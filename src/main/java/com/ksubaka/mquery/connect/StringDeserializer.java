package com.ksubaka.mquery.connect;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class StringDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
		String result = null;
		String parsedValue = parser.readValueAs(String.class);
		if (parsedValue != null && !parsedValue.isEmpty() && !parsedValue.equalsIgnoreCase("N/A")) {
			result = parsedValue;
		}
		return result;
	}
}
