package edu.mayo.informatics.fhir.uscore;

import java.io.IOException;

public class OmopExample {
    public static void main(String[] args) throws IOException {
        String inputMappingFile;
        String outputMappingFile;
        if(args.length == 0){
            inputMappingFile = "examples/src/main/resources/mapping/omop.mapping.ttl";
            outputMappingFile = "input/omop.obda";
        } else {
            inputMappingFile = args[0];
            outputMappingFile = args[1];
        }
        new Converter().go(inputMappingFile, outputMappingFile);
    }
}
