package edu.mayo.informatics.fhir.uscore;

import java.util.List;

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
            return "MappingEntry{" +
                    "target=" + target +
                    ", source='" + source + '\'' +
                    '}';
        }
    }

    public record Triple(String subject, String predicate, String object) {
        @Override
        public String toString() {
            return String.format("%s %s %s .", subject, predicate, object);
        }
    }


}


