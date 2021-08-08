package edu.mayo.informatics.fhir.uscore;

import eu.optique.r2rml.api.model.R2RMLVocabulary;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class OmopExample {

    private static final IRI RR_PREDICATE_OBJECT_MAP = iri(R2RMLVocabulary.PROP_PREDICATE_OBJECT_MAP);
    private static final IRI RR_LOGICAL_TABLE = iri(R2RMLVocabulary.PROP_LOGICAL_TABLE);
    private static final IRI RR_SUBJECT_MAP = iri(R2RMLVocabulary.PROP_SUBJECT_MAP);
    private static final IRI RR_TEMPLATE = iri(R2RMLVocabulary.PROP_TEMPLATE);
    private static final IRI RR_COLUMN = iri(R2RMLVocabulary.PROP_COLUMN);
    private static final IRI RR_DATATYPE = iri(R2RMLVocabulary.PROP_DATATYPE);

    static List<String> prefixes = Arrays.asList("""
            :		http://hl7.org/fhir/
            owl:		http://www.w3.org/2002/07/owl#
            rdf:		http://www.w3.org/1999/02/22-rdf-syntax-ns#
            xml:		http://www.w3.org/XML/1998/namespace
            xsd:		http://www.w3.org/2001/XMLSchema#
            fhir:		http://hl7.org/fhir/
            obda:		https://w3id.org/obda/vocabulary#
            rdfs:		http://www.w3.org/2000/01/rdf-schema#
            """.split("\n"));

    public static void main(String[] args) throws IOException {
        Repository repo = new SailRepository(new MemoryStore());
        try (RepositoryConnection conn = repo.getConnection()) {
            conn.add(new File("src/main/resources/mapping/omop.mapping.ttl"), null, RDFFormat.TURTLE);

            List<Resource> triplesMaps = conn.getStatements(null, RR_LOGICAL_TABLE, null)
                    .stream().map(Statement::getSubject).toList();
            AtomicInteger counter = new AtomicInteger();
            List<Mapping.Entry> entries = triplesMaps.stream()
                    .map(t -> getEntry(conn, counter.getAndIncrement(), t))
                    .toList();
            Mapping mapping = new Mapping(prefixes, entries);
            //FileWriter writer = new FileWriter("src/main/resources/mapping/omop.obda");
            FileWriter writer = new FileWriter("../input/omop.obda");
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

        Mapping.TermMap subjectTermMap = new Mapping.TermMap(template.getLabel(), Mapping.TermType.IRI, Mapping.TermMapType.TEMPLATE);

        List<Mapping.Triple> target = visit(conn, predicateObjectMap, 0, subjectTermMap);
        //target.forEach(System.out::println);

        return new Mapping.Entry("mapping" + counter, target, sourceQuery);
    }

    private static List<Mapping.Triple> visit(RepositoryConnection conn, Resource node, int level, Mapping.TermMap subjectTermMap) {

        List<Mapping.Triple> triples = new ArrayList<>();

        List<Statement> stmts = conn.getStatements(node, null, null).stream().toList();
        //stmts.forEach(x -> System.out.printf("[%d]  %s %s%n", level, subject, x));
        //System.out.println(stmts);
        //List<Resource> list = new ArrayList<>();
        for (Statement stmt : stmts) {
            Value object = stmt.getObject();
            IRI predicate = stmt.getPredicate();
            Mapping.TermMap predicateTermMap = new Mapping.TermMap(predicate.stringValue(), Mapping.TermType.IRI, Mapping.TermMapType.CONSTANT);

            if (object instanceof IRI iriObject) {
                //System.out.printf("-> %s %s %s %n", subject, predicate, iriObject);
                triples.add(new Mapping.Triple(subjectTermMap, predicateTermMap,
                        new Mapping.TermMap(iriObject.stringValue(), Mapping.TermType.IRI, Mapping.TermMapType.CONSTANT)
                ));
            }
            if (object instanceof BNode bnode) {
                Optional<Value> column = getObject(conn, bnode, RR_COLUMN);
                boolean terminal = false;
                // TODO: rr:termType
                if (column.isPresent()) {
                    //System.out.printf("-> %s %s {%s} %n", subject, predicate, column.get());
                    terminal = true;
                    triples.add(new Mapping.Triple(subjectTermMap, predicateTermMap,
                            new Mapping.TermMap(column.get().stringValue(), Mapping.TermType.LITERAL, Mapping.TermMapType.COLUMN)
                    ));
                }

                Optional<Value> template = getObject(conn, bnode, RR_TEMPLATE);
                if (template.isPresent()) {
                    terminal = true;
                    //System.out.printf("-> %s %s %s %n", subject, predicate, template.get());
                    triples.add(new Mapping.Triple(subjectTermMap, predicateTermMap,
                            new Mapping.TermMap(template.get().stringValue(), Mapping.TermType.IRI, Mapping.TermMapType.TEMPLATE)
                    ));
                }

                if (!terminal) {
                    String newSubject = "%s/%s".formatted(subjectTermMap.value(), predicate.getLocalName());
                    //System.out.printf("-> %s %s %s %n", subject, predicate, newSubject);
                    Mapping.TermMap newSubjectMap = new Mapping.TermMap(newSubject, Mapping.TermType.IRI, Mapping.TermMapType.TEMPLATE);
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
    private static Optional<Value> getObject(RepositoryConnection conn, Resource triplesMap, IRI predicate) {
        return conn.getStatements(triplesMap, predicate, null)
                .stream().map(Statement::getObject).findFirst();
    }

    private static String getSourceQuery(RepositoryConnection conn, Value logicalTable) {
        Optional<Value> sqlQuery = getSqlQuery(conn, logicalTable);
        if (sqlQuery.isPresent()) {
            return sqlQuery.get().stringValue();
        }
        Optional<Literal> tableName = getTableName(conn, logicalTable);
        return tableName.map(args -> "SELECT * FROM %s".formatted(args.getLabel())).orElse(null);
    }

    private static List<Resource> getTriplesMaps(RepositoryConnection conn) {
        String queryString = """                    
                PREFIX :       <http://hl7.org/>
                PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#>
                PREFIX rr:     <http://www.w3.org/ns/r2rml#>
                SELECT * {
                ?s a rr:TriplesMap .
                }""";
        TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        //TupleQueryResult result = tupleQuery.evaluate();
        List<Resource> values;
        try (TupleQueryResult result = tupleQuery.evaluate()) {
            values = result.stream().map(
                    b -> ((Resource) b.iterator().next().getValue())
            ).toList();
        }
        return values;
    }

    private static Value getLogicalTable(RepositoryConnection conn, Value triplesMap) {
        String queryString = """                    
                PREFIX :       <http://hl7.org/>
                PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#>
                PREFIX rr:     <http://www.w3.org/ns/r2rml#>
                SELECT * {
                ?s rr:logicalTable ?t .
                }""";
        TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        tupleQuery.setBinding("s", triplesMap);
        Value logicalTable;
        try (TupleQueryResult result = tupleQuery.evaluate()) {
            logicalTable = result.stream().map(
                    b -> b.getValue("t")
            ).findFirst().get();
        }
        return logicalTable;
    }

    private static Optional<Value> getSqlQuery(RepositoryConnection conn, Value logicalTable) {
        String queryString = """                    
                PREFIX :       <http://hl7.org/>
                PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#>
                PREFIX rr:     <http://www.w3.org/ns/r2rml#>
                SELECT * {
                ?s rr:sqlQuery ?t .
                }""";
        TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        tupleQuery.setBinding("s", logicalTable);
        Optional<Value> value;
        try (TupleQueryResult result = tupleQuery.evaluate()) {
            value = result.stream().map(
                    b -> b.getValue("t")
            ).findFirst();
        }
        return value;
    }

    private static Optional<Literal> getTableName(RepositoryConnection conn, Value logicalTable) {
        String queryString = """                    
                PREFIX :       <http://hl7.org/>
                PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#>
                PREFIX rr:     <http://www.w3.org/ns/r2rml#>
                SELECT * {
                ?s rr:tableName ?t .
                }""";
        TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        tupleQuery.setBinding("s", logicalTable);
        Optional<Literal> value;
        try (TupleQueryResult result = tupleQuery.evaluate()) {
            value = result.stream().map(
                    b -> (Literal)b.getValue("t")
            ).findFirst();
        }
        return value;
    }

    private static List<Value> getResourceIdColumns(RepositoryConnection conn) {
        String queryString = """                    
                PREFIX :       <http://hl7.org/>
                PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#>
                PREFIX rr:     <http://www.w3.org/ns/r2rml#>
                SELECT * {
                ?s :Resource.id/?p*/rr:column ?c .
                }""";
        TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        //tupleQuery.setBinding("s", logicalTable);
        List<Value> values;
        try (TupleQueryResult result = tupleQuery.evaluate()) {
            values = result.stream().map(
                    b -> b.getValue("c")
            ).toList();
        }
        return values;

    }
}
