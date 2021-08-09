package org.fhircat.mapping.old;

import java.util.Map;

public record StructureDefinition(Map<String, String> predicateMap, 
                                  Map<String, org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent> datatypeMap) {
}
