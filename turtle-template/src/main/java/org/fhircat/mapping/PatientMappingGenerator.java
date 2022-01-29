package org.fhircat.mapping;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PatientMappingGenerator {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String template = String.join("\n", Files.readAllLines(Path.of(ClassLoader.getSystemResource("examples/omop.template.obda").toURI())));
        System.out.println(template);


        for (int i = 0; i < 100; i++) {
            int patientId = 392775850 + i;
            String fileName = "src/test/resources/examples/patients/omop.%d.obda".formatted(patientId);
            System.out.println(fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            String s = template.replaceAll("\\{\\{\\{patient_id}}}", String.valueOf(patientId));
            writer.write(s);
            writer.close();
            //System.out.println(s);
        }


    }
}
