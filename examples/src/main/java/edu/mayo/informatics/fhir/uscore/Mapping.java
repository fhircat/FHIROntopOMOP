package edu.mayo.informatics.fhir.uscore;

import java.util.List;

import static java.util.stream.Collectors.joining;

public record Mapping(List<String> prefixes, List<Entry> entries) {
    @Override
    public String toString() {
        return "Mapping{" +
                "prefixes=" + prefixes +
                ", entries=" + entries +
                '}';
    }


    public record Entry(List<Mapping.Triple> target, String source) {
        @Override
        public String toString() {

            return "target\t%s\nsource\t%s".formatted(target.stream().map(Object::toString).collect(joining("")), source);
        }
    }

    public record Triple(String subject, String predicate, String object) {
        @Override
        public String toString() {
            return String.format("%s %s %s .", subject, predicate, object);
        }
    }


}


