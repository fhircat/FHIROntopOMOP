package org.fhircat.mapping.old;

import com.google.gson.JsonObject;

//sealed 
interface NodeParsedResult {

    record ColumnNode(String column) implements NodeParsedResult {
    }

    record ExpressionNode(String expression, String alias) implements NodeParsedResult {
    }

    record IntermediateNode(JsonObject o) implements NodeParsedResult {
    }
}