package org.fhircat.mapping;

import java.util.List;
import java.util.Objects;

import static eu.optique.r2rml.api.model.R2RMLVocabulary.*;
import static java.util.stream.Collectors.joining;

public record Mapping(List<String> prefixes, List<Entry> entries) {
    @Override
    public String toString() {
        return """
                [PrefixDeclaration]
                %s
                                
                [MappingDeclaration] @collection [[
                %s
                ]]
                """.formatted(
                String.join("\n", prefixes),
                entries.stream().map(Objects::toString).collect(joining("\n\n"))
        );
    }

    public static record Entry(String id, List<Mapping.Triple> target, String source) {
        @Override
        public String toString() {
            return """
                    mappingId\t%s
                    target\t%s
                    source\t%s"""
                    .formatted(id, target.stream().map(Object::toString).collect(joining(" ")), source);
        }
    }

    public enum TermMapType {
        CONSTANT, TEMPLATE, COLUMN
    }

    public enum TermType {
        IRI, BNODE, LITERAL;
        
        static TermType fromIRI(String iri) {
            return switch (iri) {
                case TERM_IRI -> IRI;
                case TERM_BLANK_NODE -> BNODE;
                case TERM_LITERAL -> LITERAL;
                default -> throw new IllegalArgumentException("unknown term type " + iri);
            };

        }
    }

    public record TermMap(String value, TermType type, TermMapType mapType, String datatype) {

        public TermMap(String value, TermType type, TermMapType mapType) {
            this(value, type, mapType, null);
        }

        @Override
        public String toString() {
            String lexical = switch (mapType) {
                case CONSTANT, TEMPLATE -> value;
                case COLUMN -> "{" + value + "}";
            };

            return switch (type) {
                case IRI -> "<" + lexical + ">";
                case BNODE -> "_:" + lexical;
                case LITERAL -> {
                    String dt = datatype == null ? "" : "^^<" + datatype + ">";
                    yield "\"%s\"%s".formatted(lexical, dt);
                }
            };
        }
    }

    public record Triple(TermMap subject, TermMap predicate, TermMap object) {
        @Override
        public String toString() {
            return String.format("%s %s %s .", subject, predicate, object);
        }
    }
}


