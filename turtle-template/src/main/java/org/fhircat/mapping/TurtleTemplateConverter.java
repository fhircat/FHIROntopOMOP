package org.fhircat.mapping;

import eu.optique.r2rml.api.model.R2RMLVocabulary;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.vocabulary.XSD;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.fhircat.mapping.Mapping.TermMap;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.fhircat.mapping.Mapping.TermMapType;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class TurtleTemplateConverter {

    private static final IRI RR_PREDICATE_OBJECT_MAP = iri(R2RMLVocabulary.PROP_PREDICATE_OBJECT_MAP);
    private static final IRI RR_LOGICAL_TABLE = iri(R2RMLVocabulary.PROP_LOGICAL_TABLE);
    private static final IRI RR_SQL_QUERY = iri(R2RMLVocabulary.PROP_SQL_QUERY);
    private static final IRI RR_TABLE_NAME = iri(R2RMLVocabulary.PROP_TABLE_NAME);
    private static final IRI RR_SUBJECT_MAP = iri(R2RMLVocabulary.PROP_SUBJECT_MAP);
    private static final IRI RR_TEMPLATE = iri(R2RMLVocabulary.PROP_TEMPLATE);
    private static final IRI RR_COLUMN = iri(R2RMLVocabulary.PROP_COLUMN);
    private static final IRI RR_DATATYPE = iri(R2RMLVocabulary.PROP_DATATYPE);


    static List<String> PREFIXES = Arrays.asList("""
            :		http://hl7.org/fhir/
            owl:		http://www.w3.org/2002/07/owl#
            rdf:		http://www.w3.org/1999/02/22-rdf-syntax-ns#
            xml:		http://www.w3.org/XML/1998/namespace
            xsd:		http://www.w3.org/2001/XMLSchema#
            fhir:		http://hl7.org/fhir/
            obda:		https://w3id.org/obda/vocabulary#
            rdfs:		http://www.w3.org/2000/01/rdf-schema#
            """.split("\n"));

    public void go(String inputMappingFile, String outputMappingFile) throws IOException {
        Repository repo = new SailRepository(new MemoryStore());
        try (RepositoryConnection conn = repo.getConnection()) {
            conn.add(new File(inputMappingFile), null, RDFFormat.TURTLE);
            List<Resource> triplesMaps = conn.getStatements(null, RR_LOGICAL_TABLE, null)
                    .stream().map(Statement::getSubject).toList();
            AtomicInteger counter = new AtomicInteger(1);
            List<Mapping.Entry> entries = triplesMaps.stream()
                    .map(t -> getEntry(conn, counter.getAndIncrement(), t))
                    .toList();
            Mapping mapping = new Mapping(PREFIXES, entries);
            FileWriter writer = new FileWriter(outputMappingFile);
            writer.write(mapping.toString());
            writer.close();
        }
    }

    @NotNull
    private static Mapping.Entry getEntry(RepositoryConnection conn, int counter, Resource triplesMap) {
        //System.out.println(triplesMap);
        Resource logicalTable = getUniqueObjectAsResource(conn, triplesMap, RR_LOGICAL_TABLE);
        //Value logicalTable = getLogicalTable(conn, triplesMap);
        //System.out.println("logicalTable = " + logicalTable);

        Resource subjectMap = getUniqueObjectAsResource(conn, triplesMap, RR_SUBJECT_MAP);
        //System.out.println("subjectMap = " + subjectMap);

        Literal template = getUniqueObjectAsLiteral(conn, subjectMap, RR_TEMPLATE);
        //System.out.println("template = " + template);

        String sourceQuery = getSourceQuery(conn, logicalTable);
        //System.out.println("sourceQuery = " + sourceQuery);

        Resource predicateObjectMap = getUniqueObjectAsResource(conn, triplesMap, RR_PREDICATE_OBJECT_MAP);

        TermMap subjectTermMap = new TermMap(template.getLabel(), Mapping.TermType.IRI, TermMapType.TEMPLATE);

        List<Mapping.Triple> target = visit(conn, predicateObjectMap, 0, subjectTermMap);
        //target.forEach(System.out::println);

        return new Mapping.Entry("mapping" + counter, target, sourceQuery);
    }

    private static List<Mapping.Triple> visit(RepositoryConnection conn, Resource node, int level, TermMap subjectTermMap) {

        List<Mapping.Triple> triples = new ArrayList<>();

        List<Statement> stmts = conn.getStatements(node, null, null).stream().toList();
        //stmts.forEach(x -> System.out.printf("[%d]  %s %s%n", level, subject, x));
        //System.out.println(stmts);
        for (Statement stmt : stmts) {
            Value object = stmt.getObject();
            IRI predicate = stmt.getPredicate();
            TermMap predicateTermMap = new TermMap(predicate.stringValue(), Mapping.TermType.IRI, TermMapType.CONSTANT);

            if (object instanceof IRI iriObject) {
                //System.out.printf("-> %s %s %s %n", subject, predicate, iriObject);
                triples.add(new Mapping.Triple(subjectTermMap, predicateTermMap,
                        new TermMap(iriObject.stringValue(), Mapping.TermType.IRI, TermMapType.CONSTANT)
                ));
            }
            if (object instanceof BNode bnode) {
                boolean terminal = false;
                Optional<Value> column = getObject(conn, bnode, RR_COLUMN);
                // TODO: rr:termType
                // TODO: rr:language
                if (column.isPresent()) {
                    //System.out.printf("-> %s %s {%s} %n", subject, predicate, column.get());
                    terminal = true;
                    String datatype = getObject(conn, bnode, RR_DATATYPE).orElse(XSD.STRING).stringValue();
                    TermMap objectTermMap = new TermMap(column.get().stringValue(),
                            Mapping.TermType.LITERAL, TermMapType.COLUMN, datatype);
                    triples.add(new Mapping.Triple(subjectTermMap, predicateTermMap, objectTermMap));
                }

                Optional<Value> template = getObject(conn, bnode, RR_TEMPLATE);
                if (template.isPresent()) {
                    terminal = true;
                    //System.out.printf("-> %s %s %s %n", subject, predicate, template.get());
                    triples.add(new Mapping.Triple(subjectTermMap, predicateTermMap,
                            new TermMap(template.get().stringValue(), Mapping.TermType.IRI, TermMapType.TEMPLATE)
                    ));
                }

                if (!terminal) {
                    String newSubject = "%s/%s".formatted(subjectTermMap.value(), predicate.getLocalName());
                    //System.out.printf("-> %s %s %s %n", subject, predicate, newSubject);
                    TermMap newSubjectMap = new TermMap(newSubject, Mapping.TermType.IRI, TermMapType.TEMPLATE);
                    triples.add(new Mapping.Triple(subjectTermMap, predicateTermMap, newSubjectMap));
                    triples.addAll(visit(conn, bnode, level + 1, newSubjectMap)); // recursion!
                }
            }
        }
        return triples;
    }


    @NotNull
    private static Resource getUniqueObjectAsResource(RepositoryConnection conn, Resource triplesMap, IRI predicate) {
        return (Resource) getObject(conn, triplesMap, predicate).get();
    }

    @NotNull
    private static Literal getUniqueObjectAsLiteral(RepositoryConnection conn, Resource triplesMap, IRI predicate) {
        return (Literal) getObject(conn, triplesMap, predicate).get();
    }

    @NotNull
    private static Optional<Value> getObject(RepositoryConnection conn, Resource subject, IRI predicate) {
        return conn.getStatements(subject, predicate, null)
                .stream().map(Statement::getObject).findFirst();
    }

    private static String getSourceQuery(RepositoryConnection conn, Resource logicalTable) {
        Optional<Value> sqlQuery = getObject(conn, logicalTable, RR_SQL_QUERY);
        if (sqlQuery.isPresent()) {
            return sqlQuery.get().stringValue();
        }
        Optional<Value> tableName = getObject(conn, logicalTable, RR_TABLE_NAME);
        return tableName.map(args -> "SELECT * FROM %s".formatted(args.stringValue())).orElse(null);
    }

}
