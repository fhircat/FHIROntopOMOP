package edu.mayo.informatics.fhir.uscore;

import java.util.List;
import java.util.Objects;

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

    public static enum TermMapType {
        CONSTANT, TEMPLATE, COLUMN
    }

    public static enum TermType {
        IRI, BNode, LITERAL
    }

    public record TermMap(String value, TermType type, TermMapType mapType) {
        @Override
        public String toString() {
            String lexical = switch (mapType) {
                case CONSTANT, TEMPLATE -> value;
                case COLUMN -> "{" + value + "}";
            };

            return switch (type) {
                case IRI -> "<" + lexical + ">";
                case BNode -> "_:" + lexical;
                case LITERAL -> "\"" + lexical + "\"";
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


