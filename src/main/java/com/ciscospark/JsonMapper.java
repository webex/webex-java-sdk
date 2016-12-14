package com.ciscospark;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonMapper {

    public <T> T readJson(Class<T> clazz, InputStream inputStream) {
        JsonParser parser = Json.createParser(inputStream);
        parser.next();
        return readObject(clazz, parser);
    }

    public <T> T readObject(Class<T> clazz, JsonParser parser) {
        try {
            T result = clazz.newInstance();
            List<Object> array = null;
            Field field = null;
            PARSER_LOOP:
            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case KEY_NAME:
                        String key = parser.getString();
                        try {
                            field = clazz.getDeclaredField(key);
                            field.setAccessible(true);
                        } catch (Exception ex) {
                            // ignore
                        }
                        break;
                    case VALUE_FALSE:
                        if (field != null) {
                            field.set(result, false);
                            field = null;
                        }
                        break;
                    case VALUE_TRUE:
                        if (field != null) {
                            field.set(result, true);
                            field = null;
                        }
                        break;
                    case VALUE_NUMBER:
                        if (field != null) {
                            if (field.getType().getName().contains("Long")) {
                                Object value = parser.getLong();
                                field.set(result, value);
                            } else {
                                Object value = (parser.isIntegralNumber() ? parser.getInt() : parser.getBigDecimal());
                                field.set(result, value);
                            }
                            field = null;
                        }
                        break;
                    case VALUE_STRING:
                        if (array != null) {
                            array.add(parser.getString());
                        } else if (field != null) {
                            if (field.getType().isAssignableFrom(String.class)) {
                                field.set(result, parser.getString());
                            } else if (field.getType().isAssignableFrom(Date.class)) {
                                field.set(result, new SimpleDateFormat(Properties.ISO8601_FORMAT).parse(parser.getString()));
                            } else if (field.getType().isAssignableFrom(URI.class)) {
                                field.set(result, URI.create(parser.getString()));
                            }
                            field = null;
                        }
                        break;
                    case VALUE_NULL:
                        field = null;
                        break;
                    case START_ARRAY:
                        array = new ArrayList<>();
                        break;
                    case END_ARRAY:
                        if (field != null) {
                            // TODO - don't hardcode class name
                            Class<?> type = field.getType();
                            String name = type.getName();
                            if (name.contains("KmsKey")) {
                                field.set(result, array.toArray(new KmsKey[array.size()]));
                            } else {
                                field.set(result, array.toArray(new Object[array.size()]));
                            }
                            field = null;
                        }
                        array = null;
                        break;
                    case END_OBJECT:
                        break PARSER_LOOP;
                    case START_OBJECT:
                        if (field != null) {
                            // TODO - don't hardcode class name
                            Class<?> aClass = getSdkClass(field);
                            Object value = readObject(aClass, parser);
                            if (array != null) {
                                array.add(value);
                            } else {
                                field.set(result, value);
                                field = null;
                            }
                        }
                        break;
                    default:
                        throw new SparkException("bad json event: " + event);
                }
            }
            return result;
        } catch (SparkException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SparkException(ex);
        }
    }

    private static Class<?> getSdkClass(Field field) {
        Class<?> type = field.getType();
        String name = type.getName();
        Class<?> aClass;
        if (name.contains("KmsKey")) {
            aClass = KmsKey.class;
        } else if (name.contains("KmsJwk")) {
            aClass = KmsJwk.class;
        } else {
            throw new SparkException("Unsupported class");
        }
        return aClass;
    }
}
