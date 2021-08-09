//package edu.mayo.informatics.fhir.uscore;
//
//import ca.uhn.fhir.context.FhirContext;
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import org.hl7.fhir.r4.model.ElementDefinition;
//import org.hl7.fhir.r4.model.StructureDefinition;
//import org.jetbrains.annotations.NotNull;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static java.util.stream.Collectors.joining;
//
//public class Example {
//
//    public static void main(String[] args) throws IOException {
//        String structureDefinitionFile = "src/main/resources/profiles/StructureDefinition-us-core-patient.json";
//        String fileName = "src/main/resources/mapping/Patient.json";
////        String structureDefinitionFile = "src/main/resources/profiles/StructureDefinition-us-core-condition.json";
////        String fileName = "src/main/resources/mapping/Condition.json";
//        generate(structureDefinitionFile, fileName);
//    }
//
//    private static void generate(String structureDefinitionFile, String fileName) throws IOException {
//        edu.mayo.informatics.fhir.uscore.StructureDefinition definition = loadStructureDefinition(structureDefinitionFile);
//        Map<String, String> fhirPathToPredicateNameMap = definition.predicateMap();
//        Map<String, ElementDefinition.TypeRefComponent> fhirPathToTypeRefComponentMap = definition.datatypeMap();
//
//        //System.out.println(fhirPathToPredicateNameMap);
//        Gson gson = new Gson();
//
//        JsonObject json = gson.fromJson(new FileReader(fileName), JsonObject.class);
//        Mapping.Entry entry = new Example().visitRoot(json, fhirPathToPredicateNameMap);
//        System.out.println(entry);
//    }
//
//    private static edu.mayo.informatics.fhir.uscore.StructureDefinition loadStructureDefinition(String structureDefinitionFile) throws IOException {
//        final String profileJson = String.join("\n",
//                Files.readAllLines(Paths.get(structureDefinitionFile)));
//        StructureDefinition profile = FhirContext.forR4().newJsonParser()
//                .parseResource(StructureDefinition.class, profileJson);
//        List<ElementDefinition> elemDefs = profile.getSnapshot().getElement();
////        ElementDefinition elementDefinition = elemDefs.get(0);
//
////        Map<String, Set<ElementDefinition>> collect = elemDefs.stream()
////                .collect(Collectors.groupingBy(ElementDefinition::getPath, Collectors.toSet()));
//
//        Map<String, String> predicateMap = elemDefs.stream()
//                .collect(Collectors.toMap(
//                        ElementDefinition::getPath,
//                        e -> e.getBase().getPath(),
//                        // there are duplicates because of the slices
//                        (existing, replacement) -> existing));
//
//        Map<String, ElementDefinition.TypeRefComponent> datatypeMap = elemDefs.stream()
//                .filter(e -> e.getType() != null && !e.getType().isEmpty())
//                .collect(Collectors.toMap(
//                        ElementDefinition::getPath,
//                        e -> e.getType().get(0),
//                        // uthere are duplicates because of the slices
//                        (existing, replacement) -> existing));
//
//
//        return new edu.mayo.informatics.fhir.uscore.StructureDefinition(predicateMap, datatypeMap);
//    }
//
//    public Mapping.Entry visitRoot(JsonObject json, Map<String, String> fhirPathToPredicateNameMap) {
//        String resourceType = json.get("resourceType").getAsString();
//
//        JsonObject vkgConfig = json.get("$vkg").getAsJsonObject();
//        String tableName = vkgConfig.get("$table").getAsString();
//        //System.out.println(tableName);
//        String baseIRI = vkgConfig.get("$baseIRI").getAsString();
//        //System.out.println(baseIRI);
//        String idColumn = vkgConfig.get("$idColumn").getAsString();
//
//        String subject = String.format(":%s/{%s}", resourceType, idColumn);
//
//        //String target = String.format("%s a fhir:%s .", subject, resourceType);
//
//        List<Mapping.Triple> targetTemplates = new ArrayList<>();
//        targetTemplates.add(new Mapping.Triple(subject, "a", resourceType));
//
//        //System.out.println(target);
//        List<NodeParsedResult.ColumnNode> columnNodes = new ArrayList<>();
//        List<NodeParsedResult.ExpressionNode> expressionNodes = new ArrayList<>();
//        visit(json, fhirPathToPredicateNameMap, resourceType, subject, targetTemplates, columnNodes, expressionNodes);
//        //targetTemplates.forEach(System.out::println);
//        //System.out.println();
//        String columns = columnNodes.stream().map(NodeParsedResult.ColumnNode::column).collect(joining(", "));
//        String expressions = expressionNodes.stream().map(n ->
//                String.format("%s AS %s", n.expression(), n.alias())
//        ).collect(joining(", "));
//
//        String projections = Stream.of(columns, expressions).filter(s -> !s.isEmpty()).collect(Collectors.joining(", "));
//        String source = String.format("SELECT %s FROM %s", projections, tableName);
//
//        //System.out.println(source);
//        //return new MappingEntry(target, source);
//        return new Mapping.Entry(targetTemplates, source);
//    }
//
//    public void visit(JsonObject mappingObject,
//                      Map<String, String> fhirPathToPredicateNameMap,
//                      String fhirPath,
//                      String subject,
//                      List<Mapping.Triple> targetTemplates,
//                      List<NodeParsedResult.ColumnNode> columnNodes,
//                      List<NodeParsedResult.ExpressionNode> expressionNodes) {
//        //System.out.println(json);
//        for (Map.Entry<String, JsonElement> entry : mappingObject.entrySet()) {
//            String key = entry.getKey();
//            JsonElement value = entry.getValue();
//            if (key.startsWith("$") || key.equals("resourceType")) {
//                //String newPath = fhirPath + "." + key;
//                //System.out.println(newPath);
//            } else {
//                //System.out.println(entry);
//                String newPath = fhirPath + "." + key;
//                String predicateName = fhirPathToPredicateNameMap.get(newPath);
//                String predicate = "fhir:" + predicateName;
//                //System.out.println(newPath + "->" + predicate);
//
//                if (value instanceof JsonObject jsonObject) {
//                    NodeParsedResult r = parseNode(jsonObject);
//                    String object;
//                    if (r instanceof NodeParsedResult.ColumnNode columnNode) {
//                        object = "{" + columnNode.column() + "}";
//                        columnNodes.add(columnNode);
//                    } else if (r instanceof NodeParsedResult.ExpressionNode expressionNode) {
//                        object = "{" + expressionNode.alias() + "}";
//                        expressionNodes.add(expressionNode);
//                    } else { // if (r instanceof NodeParsedResult.IntermediateNode intermediateNode) {
//                        object = String.format("%s/%s", subject, predicateName);
//                    }
//                    Mapping.Triple triple = new Mapping.Triple(subject, predicate, object);
//                    //System.out.println(triple);
//                    targetTemplates.add(triple);
//                    // recursion!
//                    visit(jsonObject, fhirPathToPredicateNameMap, newPath, object, targetTemplates, columnNodes, expressionNodes);
//                }
//            }
//        }
//    }
//
//    @NotNull
//    private NodeParsedResult parseNode(JsonObject jsonObject) {
//        //noinspection OptionalGetWithoutIsPresent
//        NodeParsedResult r =
//                Stream
//                        .<Function<JsonObject, Optional<? extends NodeParsedResult>>>of(
//                                this::tryParseAsColumn,
//                                this::tryParseAsExpression,
//                                this::tryParseAsIntermediateNode)
//                        .map(f -> f.apply(jsonObject))
//                        .filter(Optional::isPresent)
//                        .map(Optional::get)
//                        .findFirst()
//                        .get();
//        return r;
//    }
//
//
//    private Optional<NodeParsedResult.ColumnNode> tryParseAsColumn(JsonObject jsonObject) {
//        return Optional.of(jsonObject)
//                .filter(o -> o.has("$column"))
//                .map(o -> o.getAsJsonPrimitive("$column").getAsString())
//                .map(NodeParsedResult.ColumnNode::new);
//    }
//
//    private Optional<NodeParsedResult.ExpressionNode> tryParseAsExpression(JsonObject jsonObject) {
//        return Optional.of(jsonObject)
//                .filter(o -> o.has("$expression"))
//                .map(o -> new NodeParsedResult.ExpressionNode(
//                        o.getAsJsonPrimitive("$expression").getAsString(),
//                        o.getAsJsonPrimitive("$alias").getAsString()
//                ));
//        //.map(ExpressionNode::new);
//    }
//
//    private Optional<NodeParsedResult.IntermediateNode> tryParseAsIntermediateNode(JsonObject jsonObject) {
//        return Optional.of(jsonObject)
//                //.filter(o -> o.has("$expression"))
//                .map(NodeParsedResult.IntermediateNode::new);
//        //.map(ExpressionNode::new);
//    }
//}
