package com.ciscospark;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.*;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private static final String TRACKING_ID = "TrackingID";
    private static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private final URI baseUri;
    private final URI redirectUri;
    private final String clientId;
    private final String clientSecret;
    private final Logger logger;
    private final Boolean enableEndToEndEncryption;

    private String accessToken;
    private String authCode;
    private String refreshToken;

    public Client(URI baseUri, String authCode, URI redirectUri, String accessToken, String refreshToken, String clientId, String clientSecret, Logger logger, Boolean enableEndToEndEncryption) {
        this.authCode = authCode;
        this.redirectUri = redirectUri;
        this.baseUri = baseUri;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.logger = logger;
        this.enableEndToEndEncryption = enableEndToEndEncryption;
    }

    public <T> T post(Class<T> clazz, String path, T body) {
        return readJson(clazz, request("POST", path, null, body));
    }

    public <T> T post(Class<T> clazz, URL url, T body) {
        return readJson(clazz, request(url, "POST", body).inputStream);
    }

    // TODO
    public <U, V> V posts(Class<V> clazz, String path, U body) {
        return readJson(clazz, request("POST", path, null, body));
    }

    public <T> T put(Class<T> clazz, String path, T body) {
        return readJson(clazz, request("PUT", path, null, body));
    }

    public <T> T put(Class<T> clazz, URL url, T body) {
        return readJson(clazz, request(url, "PUT", body).inputStream);
    }

    public <T> T get(Class<T> clazz, String path, List<String[]> params) {
        return readJson(clazz, request("GET", path, params, null));
    }

    public <T> T get(Class<T> clazz, URL url) {
        return readJson(clazz, request(url, "GET", null).inputStream);
    }

    public <T> Iterator<T> list(Class<T> clazz, String path, List<String[]> params) {
        return new PagingIterator<>(clazz, getUrl(path, params));
    }

    public <T> Iterator<T> list(Class<T> clazz, URL url) {
        return new PagingIterator<>(clazz, url);
    }

    public void delete(String path) {
        delete(getUrl(path, null));
    }

    public void delete(URL url) {
        try {
            HttpURLConnection connection = getConnection(url);
            connection.setRequestMethod("DELETE");
            int responseCode = connection.getResponseCode();
            checkForErrorResponse(connection, responseCode);
        } catch (IOException ex) {
            throw new SparkException(ex);
        }
    }

    public <T> LinkedResponse<List<T>> paginate(Class<T> clazz, URL url) {
        Function<InputStream, List<T>> function = istream -> {
            JsonParser parser = Json.createParser(istream);
            scrollToItemsArray(parser);

            List<T> result = new ArrayList<>();
            for (JsonParser.Event event = parser.next();
                 event == JsonParser.Event.START_OBJECT;
                 event = parser.next()) {
                result.add(readObject(clazz, parser));
            }
            return result;
        };

        try {
            return new LinkedResponse<>(this, url, function);
        } catch (IOException e) {
            throw new SparkException("io error", e);
        }
    }

    public <T> LinkedResponse<List<T>> paginate(Class<T> clazz, String paths, List<String[]> params) {
        URL url = getUrl(paths, params);
        return paginate(clazz, url);
    }

    private <T> InputStream request(String method, String path, List<String[]> params, T body) {
        URL url = getUrl(path, params);
        return request(url, method, body).inputStream;
    }

    public <T> Response request(URL url, String method, T body) {
        if (accessToken == null) {
            if (!authenticate()) {
                throw new NotAuthenticatedException();
            }
        }

        try {
            return doRequest(url, method, body);
        } catch (NotAuthenticatedException ex) {
            if (authenticate()) {
                return doRequest(url, method, body);
            } else {
                throw ex;
            }
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    private boolean authenticate() {
        if (clientId != null && clientSecret != null) {
            if (authCode != null && redirectUri != null) {
                log(Level.FINE, "Requesting access token");
                URL url = getUrl("/access_token", null);
                AccessTokenRequest body = new AccessTokenRequest();
                body.setGrant_type("authorization_code");
                body.setClient_id(clientId);
                body.setClient_secret(clientSecret);
                body.setCode(authCode);
                body.setRedirect_uri(redirectUri);
                Response response = doRequest(url, "POST", body);
                AccessTokenResponse responseBody = readJson(AccessTokenResponse.class, response.inputStream);
                accessToken = responseBody.getAccess_token();
                refreshToken = responseBody.getRefresh_token();
                authCode = null;
                return true;
            } else if (refreshToken != null) {
                log(Level.FINE, "Refreshing access token");
                URL url = getUrl("/access_token", null);
                AccessTokenRequest body = new AccessTokenRequest();
                body.setClient_id(clientId);
                body.setClient_secret(clientSecret);
                body.setRefresh_token(refreshToken);
                body.setGrant_type("refresh_token");
                Response response = doRequest(url, "POST", body);
                AccessTokenResponse responseBody = readJson(AccessTokenResponse.class, response.inputStream);
                accessToken = responseBody.getAccess_token();
                return true;
            }
        }
        return false;
    }

    private void log(Level level, String msg, Object... args) {
        if (logger != null && logger.isLoggable(level)) {
            logger.log(level, msg, args);
        }
    }

    private <T> Response doRequest(URL url, String method, T body) {
        try {
            HttpURLConnection connection = getConnection(url);
            String trackingId = connection.getRequestProperty(TRACKING_ID);
            connection.setRequestMethod(method);
            if (logger != null && logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Request {0}: {1} {2}",
                        new Object[]{trackingId, method, connection.getURL().toString()});
            }
            if (body != null) {
                connection.setDoOutput(true);
                if (logger != null && logger.isLoggable(Level.FINEST)) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    writeJson(body, byteArrayOutputStream);
                    logger.log(Level.FINEST, "Request Body {0}: {1}",
                            new Object[]{trackingId, byteArrayOutputStream.toString()});
                    byteArrayOutputStream.writeTo(connection.getOutputStream());
                } else {
                    writeJson(body, connection.getOutputStream());
                }
            }

            int responseCode = connection.getResponseCode();
            if (logger != null && logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Response {0}: {1} {2}",
                        new Object[]{trackingId, responseCode, connection.getResponseMessage()});
            }
            checkForErrorResponse(connection, responseCode);

            if (logger != null && logger.isLoggable(Level.FINEST)) {
                InputStream inputStream = logResponse(trackingId, connection.getInputStream());
                return new Response(connection, inputStream);
            } else {
                InputStream inputStream = connection.getInputStream();
                return new Response(connection, inputStream);

            }
        } catch (IOException ex) {
            throw new SparkException("io error", ex);
        }
    }

    private InputStream logResponse(String trackingId, InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[16 * 1024];
        int count;
        while ((count = inputStream.read(buf)) != -1) {
            byteArrayOutputStream.write(buf, 0, count);
        }
        logger.log(Level.FINEST, "Response Body {0}: {1}",
                new Object[]{trackingId, byteArrayOutputStream.toString()});
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private void checkForErrorResponse(HttpURLConnection connection, int responseCode) throws NotAuthenticatedException, IOException {
        if (responseCode == 401) {
            throw new NotAuthenticatedException();
        } else if (responseCode < 200 || responseCode >= 400) {
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


            Function<InputStream, Object> processor = stream -> {
                ErrorMessage errorMessage = parseErrorMessage(stream);
                if (errorMessage != null) {
                    errorMessageBuilder.append(": ");
                    errorMessageBuilder.append(errorMessage.getMessage());
                }
                return null;
            };

            if (logger != null && logger.isLoggable(Level.FINEST)) {
                InputStream inputStream = logResponse(connection.getRequestProperty(TRACKING_ID), connection.getErrorStream());
                processor.apply(inputStream);
            } else {
                processor.apply(connection.getErrorStream());
            }

            throw new SparkException(errorMessageBuilder.toString());
        }
    }

    private static ErrorMessage parseErrorMessage(InputStream errorStream) {
        try {
            if (errorStream == null) {
                return null;
            }
            JsonReader reader = Json.createReader(errorStream);
            JsonObject jsonObject = reader.readObject();
            ErrorMessage result = new ErrorMessage();
            result.setMessage(jsonObject.getString("message"));
            result.setTrackingId(jsonObject.getString("trackingId"));
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-type", "application/json");
        if (accessToken != null) {
            String authorization = accessToken;
            if (!authorization.startsWith("Bearer ")) {
                authorization = "Bearer " + accessToken;
            }
            connection.setRequestProperty("Authorization", authorization);
        }
        // TODO - I don't like this, should pass in the header map as a whole
        if (enableEndToEndEncryption) {
            connection.setRequestProperty("X-Spark-Content-Type", "application/json+spark");
        }
        connection.setRequestProperty(TRACKING_ID, UUID.randomUUID().toString());
        return connection;
    }

    private static <T> T readJson(Class<T> clazz, InputStream inputStream) {
        JsonParser parser = Json.createParser(inputStream);
        parser.next();
        return readObject(clazz, parser);
    }

    private static <T> T readObject(Class<T> clazz, JsonParser parser) {
        try {
            T result = clazz.newInstance();
            List<String> array = null;
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
                                field.set(result, new SimpleDateFormat(ISO8601_FORMAT).parse(parser.getString()));
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
                            field.set(result, array.toArray(new String[array.size()]));
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
            params.forEach(param ->
                    urlStringBuilder
                            .append(encode(param[0]))
                            .append("=")
                            .append(encode(param[1]))
                            .append("&")
            );
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
                } else if (type == Date.class) {
                    jsonGenerator.write(field.getName(), new SimpleDateFormat(ISO8601_FORMAT).format((Date) value));
                } else if (type == URI.class) {
                    jsonGenerator.write(field.getName(), value.toString());
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

    public class PagingIterator<T> implements Iterator<T> {
        private final Class<T> clazz;
        private URL url;
        private HttpURLConnection connection;
        private JsonParser parser;
        T current;

        public PagingIterator(Class<T> clazz, URL url) {
            this.clazz = clazz;
            this.url = url;
        }

        @Override
        public boolean hasNext() {
            try {
                if (current == null) {
                    if (parser == null) {
                        Response response = request(url, "GET", null);
                        InputStream inputStream = response.inputStream;
                        connection = response.connection;
                        parser = Json.createParser(inputStream);

                        scrollToItemsArray(parser);
                    }

                    JsonParser.Event event = parser.next();
                    if (event != JsonParser.Event.START_OBJECT) {
                        HttpURLConnection next = getLink(connection, "next");
                        if (next == null || (next.getURL().equals(url))) {
                            return false;
                        } else {
                            connection = next;
                            url = connection.getURL();
                            parser = null;
                            return hasNext();
                        }
                    }
                    current = readObject(clazz, parser);
                }
                return current != null;
            } catch (IOException ex) {
                throw new SparkException(ex);
            }
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
    }

    private void scrollToItemsArray(JsonParser parser) {
        JsonParser.Event event;
        while (parser.hasNext()) {
            event = parser.next();
            if (event == JsonParser.Event.KEY_NAME && parser.getString().equals("items")) {
                break;
            }
        }

        event = parser.next();
        if (event != JsonParser.Event.START_ARRAY) {
            throw new SparkException("bad json");
        }
    }

    private static final Pattern linkPattern = Pattern.compile("\\s*<(\\S+)>\\s*;\\s*rel=\"(\\S+)\",?");

    private HttpURLConnection getLink(HttpURLConnection connection, String rel) throws IOException {
        String link = connection.getHeaderField("Link");
        return parseLinkHeader(link, rel);
    }

    private HttpURLConnection parseLinkHeader(String link, String desiredRel) throws IOException {
        HttpURLConnection result = null;
        if (link != null && !"".equals(link)) {
            Matcher matcher = linkPattern.matcher(link);
            while (matcher.find()) {
                String url = matcher.group(1);
                String foundRel = matcher.group(2);
                if (desiredRel.equals(foundRel)) {
                    result = getConnection(new URL(url));
                    break;
                }
            }
        }
        return result;
    }
}
