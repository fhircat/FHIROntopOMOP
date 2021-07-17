package edu.mayo.informatics.fhir.uscore;

import ca.uhn.fhir.context.FhirContext;
import com.google.gson.Gson;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.StructureDefinition;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Example {

    public static void main(String[] args) throws IOException {

        String structureDefinitionFile = "src/main/resources/profiles/StructureDefinition-us-core-patient.json";
        Map<String, String> fhirPathToPredicateNameMap = loadStructureDefinition(structureDefinitionFile);

        //System.out.println(fhirPathToPredicateNameMap);

        Gson gson = new Gson();
        String fileName = "src/main/resources/mapping/Patient.json";
        Map<String, ?> json = gson.fromJson(new FileReader(fileName), Map.class);
        visitRoot(json, fhirPathToPredicateNameMap);
    }

    private static Map<String, String> loadStructureDefinition(String structureDefinitionFile) throws IOException {
        final String profileJson = String.join("\n",
                Files.readAllLines(Paths.get(structureDefinitionFile)));
        StructureDefinition profile = FhirContext.forR4().newJsonParser()
                .parseResource(StructureDefinition.class, profileJson);
        List<ElementDefinition> elemDefs = profile.getSnapshot().getElement();
//        ElementDefinition elementDefinition = elemDefs.get(0);

//        Map<String, Set<ElementDefinition>> collect = elemDefs.stream()
//                .collect(Collectors.groupingBy(ElementDefinition::getPath, Collectors.toSet()));

        return elemDefs.stream()
                .collect(Collectors.toMap(
                        ElementDefinition::getPath,
                        e -> e.getBase().getPath(),
                        // there are duplicates because of the slices
                        (existing, replacement) -> existing));
    }

    public static void visitRoot(Map<String, ?> json, Map<String, String> fhirPathToPredicateNameMap) {
        String resourceType = (String) json.get("resourceType");

        Map<String, ?> vkgConfig = (Map<String, ?>) json.get("$vkg");
        String tableName = (String) vkgConfig.get("$table");
        //System.out.println(tableName);
        String baseIRI = (String) vkgConfig.get("$baseIRI");
        //System.out.println(baseIRI);
        String idColumn = (String) vkgConfig.get("$idColumn");

        String subject = String.format(":%s/{%s}", resourceType, idColumn);

        StringBuilder target = new StringBuilder(String.format("%s a fhir:%s .", subject, resourceType));
        String source = String.format("SELECT * FROM %s", tableName);

        System.out.println(target);
        System.out.println(source);

        visit(json, fhirPathToPredicateNameMap, resourceType, subject);
    }

    public static void visit(Map<String, ?> mappingObject,
                             Map<String, String> fhirPathToPredicateNameMap,
                             String fhirPath, String subject) {
        //System.out.println(json);
        for (Map.Entry<String, ?> entry : mappingObject.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.startsWith("$")) {
                //String newPath = fhirPath + "." + key;
                //System.out.println(newPath);
            } else if (key.equals("resourceType")) {
                // nothing
            } else {
                //System.out.println(entry);
                String newPath = fhirPath + "." + key;
                String predicateName = fhirPathToPredicateNameMap.get(newPath);
                String predicate = "fhir:"+predicateName;
                //System.out.println(newPath + "->" + predicate);
                String object = null;
                if (value instanceof Map){
                    Map<String, ?> map = (Map<String, ?>) value;
                    if (map.entrySet().size() == 1 && map.keySet().iterator().next().startsWith("$")){
                        object = "{" + (String) map.entrySet().iterator().next().getValue() + "}";    
                    } else {
                        object = String.format("%s/%s", subject, predicateName);
                    }                     
                }
                String triple = String.format("%s %s %s .", subject, predicate, object);
                System.out.println(triple);
                if (value instanceof Map) {
                    visit((Map) entry.getValue(), fhirPathToPredicateNameMap, newPath, object);
                }
            }

        }
    }
}
