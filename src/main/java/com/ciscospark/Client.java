package com.ciscospark;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.*;
import java.util.*;

/**
 * Created on 11/24/15.
 */
class Client {
    final URI baseUri;

    String accessToken;
    final String refreshToken;
    final String clientId;
    final String clientSecret;

    Client(URI baseUri, String accessToken, String refreshToken, String clientId, String clientSecret) {
        this.baseUri = baseUri;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    <T> T post(Class<T> clazz, String path, T body) {
        try {
            HttpURLConnection connection = getConnection(path, null);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream ostream = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    System.out.write(b);
                    connection.getOutputStream().write(b);
                }
            };
            writeJson(body, ostream);
            ostream.flush();

            int responseCode = connection.getResponseCode();
            checkForErrorResponse(connection, responseCode);
            InputStream inputStream = getInputStream(connection);
            return readJson(clazz, inputStream);
        } catch (IOException ex) {
            throw new SparkException("io error", ex);
        }
    }


    <T> T put(Class<T> clazz, String path, T body) {
        try {
            HttpURLConnection connection = getConnection(path, null);
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");

            OutputStream ostream = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    System.out.write(b);
                    connection.getOutputStream().write(b);
                }
            };
            writeJson(body, ostream);
            ostream.flush();

            int responseCode = connection.getResponseCode();
            checkForErrorResponse(connection, responseCode);
            InputStream inputStream = getInputStream(connection);
            return readJson(clazz, inputStream);
        } catch (IOException ex) {
            throw new SparkException("io error", ex);
        }
    }

    private void checkForErrorResponse(HttpURLConnection connection, int responseCode) {
        if (responseCode < 200 || responseCode > 300) {
            StringBuilder errorMessageBuilder = new StringBuilder("bad response code ");
            errorMessageBuilder.append(responseCode);
            try {
                String responseMessage = connection.getResponseMessage();
                if (responseMessage != null) {
                    errorMessageBuilder.append(" ");
                    errorMessageBuilder.append(responseMessage);
                }
            } catch (IOException ex) {
                // ignore
            }

            ErrorMessage errorMessage = parseErrorMessage(connection);
            if (errorMessage != null) {
                errorMessageBuilder.append(": ");
                errorMessageBuilder.append(errorMessage.message);
            }
            throw new SparkException(errorMessageBuilder.toString());
        }
    }

    static class ErrorMessage {
        String message;
        String[] errors;
        String trackingId;
    }

    private ErrorMessage parseErrorMessage(HttpURLConnection connection) {
        try {
            InputStream inputStream = getErrorStream(connection);
            if (inputStream != null) {
                JsonReader reader = Json.createReader(inputStream);
                JsonObject jsonObject = reader.readObject();
                ErrorMessage result = new ErrorMessage();
                result.message = jsonObject.getString("message");
                result.trackingId = jsonObject.getString("trackingId");
                return result;
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    <T> T get(Class<T> clazz, String path, List<String[]> params) {
        try {
            HttpURLConnection connection = getConnection(path, null);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            checkForErrorResponse(connection, responseCode);
            InputStream inputStream = getInputStream(connection);
            return readJson(clazz, inputStream);
        } catch (IOException ex) {
            throw new SparkException("io error", ex);
        }
    }

    <T> Iterator<T> list(Class<T> clazz, String path, List<String[]> params) {
        try {
            HttpURLConnection connection = getConnection(path, params);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            checkForErrorResponse(connection, responseCode);
            InputStream inputStream = getInputStream(connection);
            JsonParser parser = Json.createParser(inputStream);

            JsonParser.Event event;
            while (parser.hasNext()) {
                event = parser.next();
                if (event == JsonParser.Event.KEY_NAME &&  parser.getString().equals("items")) {
                    break;
                }
            }

            event = parser.next();
            if (event != JsonParser.Event.START_ARRAY) {
                throw new SparkException("bad json");
            }

            return new Iterator<T>() {
                T current;

                @Override
                public boolean hasNext() {
                    if (current == null) {
                        JsonParser.Event event = parser.next();
                        if (event != JsonParser.Event.START_OBJECT) {
                            return false;
                        }
                        current = readObject(clazz, parser);
                    }
                    return current != null;
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }

                    try {
                        return current;
                    } finally {
                        current = null;
                    }
                }
            };
        } catch (IOException ex) {
            throw new SparkException("io error", ex);
        }
    }

    private InputStream getInputStream(final HttpURLConnection connection) {
        return new InputStream() {
                    @Override
                    public int read(byte[] b, int off, int len) throws IOException {
                        int boo = connection.getInputStream().read(b, off, len);
                        if (boo != -1) {
                            System.out.write(b, off, boo);
                        }
                        return boo;
                    }

                    @Override
                    public int read() throws IOException {
                        int boo = connection.getInputStream().read();
                        if (boo != -1) {
                            System.out.write(boo);
                        }
                        return boo;
                    }
                };
    }


    private InputStream getErrorStream(final HttpURLConnection connection) {
        return new InputStream() {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                int boo = connection.getErrorStream().read(b, off, len);
                if (boo != -1) {
                    System.err.write(b, off, boo);
                }
                return boo;
            }

            @Override
            public int read() throws IOException {
                int boo = connection.getErrorStream().read();
                if (boo != -1) {
                    System.err.write(boo);
                }
                return boo;
            }
        };
    }

    void delete(String path) {
        try {
            HttpURLConnection connection = getConnection(path, null);
            connection.setRequestMethod("DELETE");
            int responseCode = connection.getResponseCode();
            checkForErrorResponse(connection, responseCode);
        } catch (IOException ex) {
            throw new SparkException(ex);
        }
    }

    private HttpURLConnection getConnection(String path, List<String[]> params) throws IOException {
        URL url = getUrl(path, params);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-type", "application/json");
        String authorization = accessToken;
        if (!authorization.startsWith("Bearer ")) {
            authorization = "Bearer " + accessToken;
        }
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("TrackingID", UUID.randomUUID().toString());
        return connection;
    }



    private <T> T readJson(Class<T> clazz, InputStream inputStream) {
        JsonParser parser = Json.createParser(inputStream);
        parser.next();
        return readObject(clazz, parser);
    }

    private <T> T readObject(Class<T> clazz, JsonParser parser) {
        try {
            T result = clazz.newInstance();
            List<String> array = null;
            Field field = null;
            PARSER_LOOP: while (parser.hasNext()) {
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
                            Object value = (parser.isIntegralNumber() ? parser.getInt() : parser.getBigDecimal());
                            field.set(result, value);
                            field = null;
                        }
                        break;
                    case VALUE_STRING:
                        if (array != null) {
                            array.add(parser.getString());
                        } else if (field != null) {
                            field.set(result, parser.getString());
                            field = null;
                        }
                        break;
                    case VALUE_NULL:
                        field = null;
                        break;
                    case START_ARRAY:
                        array = new ArrayList<String>();
                        break;
                    case END_ARRAY:
                        if (field != null) {
                            field.set(result, array.toArray(new String[] {}));
                            field = null;
                        }
                        array = null;
                        break;
                    case END_OBJECT:
                        break PARSER_LOOP;
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

    private URL getUrl(String path, List<String[]> params) {
        StringBuilder urlStringBuilder = new StringBuilder(baseUri.toString() + path);
        if (params != null) {
            urlStringBuilder.append("?");
            params.forEach(param -> {
                urlStringBuilder
                        .append(param[0])
                        .append("=")
                        .append(encode(param[1]))
                        .append("&");
            });
        }
        URL url;
        try {
            url = new URL(urlStringBuilder.toString());
        } catch (MalformedURLException e) {
            throw new SparkException("bad url: " + urlStringBuilder, e);
        }
        return url;
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new SparkException(ex);
        }
    }

    private void writeJson(Object body, OutputStream ostream) {
        JsonGenerator jsonGenerator = Json.createGenerator(ostream);
        jsonGenerator.writeStartObject();
        for (Field field : body.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(body);
                if (value == null) {
                    continue;
                }

                AnnotatedType annotatedType = field.getAnnotatedType();
                Type type = annotatedType.getType();
                if (type == String.class) {
                    jsonGenerator.write(field.getName(), (String) value);
                } else if (type == Integer.class) {
                    jsonGenerator.write(field.getName(), (Integer) value);
                } else if (type == BigDecimal.class) {
                    jsonGenerator.write(field.getName(), (BigDecimal) value);
                } else if (type == Boolean.class) {
                    jsonGenerator.write(field.getName(), (Boolean) value);
                } else if (type == String[].class) {
                    jsonGenerator.writeStartArray(field.getName());
                    for (String st : (String[]) value) {
                        jsonGenerator.write(st);
                    }
                    jsonGenerator.writeEnd();
                }
            } catch (IllegalAccessException ex) {
                // ignore
            }
        }
        jsonGenerator.writeEnd();
        jsonGenerator.flush();
        jsonGenerator.close();
    }

}
