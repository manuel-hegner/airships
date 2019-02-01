package io.github.manuelhegner.airships.util.io;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class Jackson {
	public static final ObjectMapper MAPPER;

	static {
		MAPPER = configure(new ObjectMapper());
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ObjectMapper> T configure(T objectMapper) {
		return (T)objectMapper
				.enable(MapperFeature.PROPAGATE_TRANSIENT_MARKER)
				.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
				.enable(Feature.ALLOW_UNQUOTED_FIELD_NAMES)
				.enable(Feature.ALLOW_COMMENTS)
				.enable(Feature.ALLOW_UNQUOTED_CONTROL_CHARS)
				.enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
				.enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
				.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
				.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
				.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
				.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS)
				.disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.setLocale(Locale.ROOT)
				.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
				.registerModule(new Jdk8Module())
				.setSerializationInclusion(Include.NON_NULL)
				.setVisibility(objectMapper.getVisibilityChecker()
					.withFieldVisibility(Visibility.PUBLIC_ONLY)
				)
				.setDefaultPrettyPrinter(new DefaultPrettyPrinter()
					.withObjectIndenter(new DefaultIndenter("\t", "\n"))
					.withArrayIndenter(new DefaultIndenter("\t", "\n"))
				);
	}
}
