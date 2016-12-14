package com.ciscospark;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class KmsApi {

    private static ObjectMapper mapper;
    private static SimpleDateFormat rfc3339DateTimeFormat;

    static {
        rfc3339DateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        rfc3339DateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    static String convertToJson(Object value) throws Exception {
        return getMapper().writeValueAsString(value);
    }

    static ObjectMapper getMapper() {
        if (null == mapper) {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            SimpleModule dateModule = new SimpleModule("rfc3339-date-time");
            dateModule.addSerializer(new DateSerializer(false, rfc3339DateTimeFormat));
            mapper.registerModule(dateModule);
        }
        return mapper;
    }
}
