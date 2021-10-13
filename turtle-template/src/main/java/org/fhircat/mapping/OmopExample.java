package org.fhircat.mapping;

import java.io.IOException;

public class OmopExample {
    public static void main(String[] args) throws IOException {
        String inputMappingFile;
        String outputMappingFile;
        if (args.length == 0) {
            inputMappingFile = "turtle-template/src/main/resources/mapping/omop.mapping.ttl";
            outputMappingFile = "input/omop.obda";
        } else {
            inputMappingFile = args[0];
            outputMappingFile = args[1];
        }
        new TurtleTemplateConverter().go(inputMappingFile, outputMappingFile);
    }
}
