package com.ciscospark;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.*;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
class Client {
    private static final String TRACKING_ID = "TrackingID";
    public static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    final URI baseUri;

    String accessToken;
    String authCode;
    final URI redirectUri;
    String refreshToken;
    final String clientId;
    final String clientSecret;
    final Logger logger;

    Client(URI baseUri, String authCode, URI redirectUri, String accessToken, String refreshToken, String clientId, String clientSecret, Logger logger) {
        this.authCode = authCode;
        this.redirectUri = redirectUri;
        this.baseUri = baseUri;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.logger = logger;
    }

    <T> T post(Class<T> clazz, String path, T body) {
        return readJson(clazz, request("POST", path, null, body));
    }

    <T> T post(Class<T> clazz, URL url, T body) {
        return readJson(clazz, request(url, "POST", body).inputStream);
    }

    <T> T put(Class<T> clazz, String path, T body) {
        return readJson(clazz, request("PUT", path, null, body));
    }

    <T> T put(Class<T> clazz, URL url, T body) {
        return readJson(clazz, request(url, "PUT", body).inputStream);
    }

    <T> T get(Class<T> clazz, String path, List<String[]> params) {
        return readJson(clazz, request("GET", path, params, null));
    }

    <T> T get(Class<T> clazz, URL url) {
        return readJson(clazz, request(url, "GET", null).inputStream);
    }

    <T> Iterator<T> list(Class<T> clazz, String path, List<String[]> params) {
        return new PagingIterator<T>(clazz, getUrl(path, params));
    }

    <T> Iterator<T> list(Class<T> clazz, URL url) {
        return new PagingIterator<T>(clazz, url);
    }

    void delete(String path) {
        delete(getUrl(path, null));
    }

    void delete(URL url) {
        try {
            HttpsURLConnection connection = getConnection(url);
            connection.setRequestMethod("DELETE");
            int responseCode = connection.getResponseCode();
            checkForErrorResponse(connection, responseCode);
        } catch (IOException ex) {
            throw new SparkException(ex);
        }
    }


    public <T> LinkedResponse<List<T>> paginate(final Class<T> clazz, URL url) {
        LinkedResponse.BodyCreator<List<T>> function = new LinkedResponse.BodyCreator<List<T>>() {
            @Override
            public List<T> create(InputStream istream) {
                JsonParser parser = Json.createParser(istream);
                Client.this.scrollToItemsArray(parser);

                List<T> result = new ArrayList<T>();
                for (JsonParser.Event event = parser.next();
                     event == JsonParser.Event.START_OBJECT;
                     event = parser.next()) {
                    result.add(readObject(clazz, parser));
                }
                return result;
            }
        };

        try {
            return new LinkedResponse<List<T>>(this, url, function);
        } catch (IOException e) {
            throw new SparkException("io error", e);
        }
    }

    public <T> LinkedResponse<List<T>> paginate(Class<T> clazz, String paths, List<String[]> params) {
        URL url = getUrl(paths, params);
        return paginate(clazz, url);
    }


    <T> InputStream request(String method, String path, List<String[]> params, T body) {
        URL url = getUrl(path, params);
        return request(url, method, body).inputStream;
    }
    static class Response {
        HttpsURLConnection connection;
        InputStream inputStream;

        public Response(HttpsURLConnection connection, InputStream inputStream) {
            this.connection = connection;
            this.inputStream = inputStream;
        }
    }

    <T> Response request(URL url, String method, T body) {
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

    private boolean authenticate() {
        if (clientId != null && clientSecret != null) {
            if (authCode != null && redirectUri != null) {
                log(Level.FINE, "Requesting access token");
                URL url = getUrl("/access_token",null);
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
                URL url = getUrl("/access_token",null);
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
            HttpsURLConnection connection = getConnection(url);
            String trackingId = connection.getRequestProperty(TRACKING_ID);
            connection.setRequestMethod(method);
            if (logger != null && logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Request {0}: {1} {2}",
                        new Object[] { trackingId, method, connection.getURL().toString() });
            }
            if (body != null) {
                connection.setDoOutput(true);
                if (logger != null && logger.isLoggable(Level.FINEST)) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    writeJson(body, byteArrayOutputStream);
                    logger.log(Level.FINEST, "Request Body {0}: {1}",
                            new Object[] { trackingId, byteArrayOutputStream.toString() });
                    byteArrayOutputStream.writeTo(connection.getOutputStream());
                } else {
                    writeJson(body, connection.getOutputStream());
                }
            }

            int responseCode = connection.getResponseCode();
            if (logger != null && logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Response {0}: {1} {2}",
                        new Object[] { trackingId, responseCode, connection.getResponseMessage() });
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

    private void checkForErrorResponse(HttpsURLConnection connection, int responseCode) throws NotAuthenticatedException, IOException {
        if (responseCode == 401) {
            throw new NotAuthenticatedException();
        } else if (responseCode < 200 || responseCode >= 400) {
            final StringBuilder errorMessageBuilder = new StringBuilder("bad response code ");
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


            ErrorMessage errorMessage;
            if (logger != null && logger.isLoggable(Level.FINEST)) {
                InputStream inputStream = logResponse(connection.getRequestProperty(TRACKING_ID), connection.getErrorStream());
                errorMessage = parseErrorMessage(inputStream);
            } else {
                errorMessage = parseErrorMessage(connection.getErrorStream());
            }

            if (errorMessage != null) {
                errorMessageBuilder.append(": ");
                errorMessageBuilder.append(errorMessage.message);
            }

            throw new SparkException(errorMessageBuilder.toString(), responseCode);
        }
    }

    static class ErrorMessage {
        String message;
        String trackingId;
    }

    private static ErrorMessage parseErrorMessage(InputStream errorStream) {
        try {
            if (errorStream == null) {
                return null;
            }
            JsonReader reader = Json.createReader(errorStream);
            JsonObject jsonObject = reader.readObject();
            ErrorMessage result = new ErrorMessage();
            result.message = jsonObject.getString("message");
            result.trackingId = jsonObject.getString("trackingId");
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    private HttpsURLConnection getConnection(URL url) throws IOException {
        try {
            SSLContext ssl = SSLContext.getInstance("TLSv1.2");
            ssl.init(null, null, new SecureRandom());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(ssl.getSocketFactory());
            connection.setRequestProperty("Content-type", "application/json");
            if (accessToken != null) {
                String authorization = accessToken;
                if (!authorization.startsWith("Bearer ")) {
                    authorization = "Bearer " + accessToken;
                }
                connection.setRequestProperty("Authorization", authorization);
            }
            connection.setRequestProperty(TRACKING_ID, UUID.randomUUID().toString());
            return connection;
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }


    private static <T> T readJson(Class<T> clazz, InputStream inputStream) {
        JsonParser parser = Json.createParser(inputStream);
        parser.next();
        return readObject(clazz, parser);
    }

    private static <T> T readObject(Class<T> clazz, JsonParser parser) {
        try {
            T result = clazz.newInstance();
            List<Object> list = null;
            Field field = null;
            String key = "";
            PARSER_LOOP: while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case KEY_NAME:
                        key = parser.getString();
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
                        if (list != null) {
                            list.add(parser.getString());
                        } else if (field != null) {
                            if (field.getType().isAssignableFrom(String.class)) {
                                field.set(result, parser.getString());
                            } else if (field.getType().isAssignableFrom(Date.class)) {
                                DateFormat dateFormat = new SimpleDateFormat(ISO8601_FORMAT);
                                field.set(result, dateFormat.parse(parser.getString()));
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
                        list = new ArrayList<Object>();
                        break;
                    case END_ARRAY:
                        if (field != null) {
                            Class itemClazz;
                            if (field.getType().equals(String[].class)) {
                                itemClazz = String.class;
                            } else if (field.getType().equals(URI[].class)) {
                                itemClazz = URI.class;
                                ListIterator<Object> iterator = list.listIterator();
                                while (iterator.hasNext()) {
                                    Object next = iterator.next();
                                    iterator.set(URI.create(next.toString()));
                                }
                            } else if (field.getType().getComponentType() != null /* this is an array class */ ) {
                                itemClazz = field.getType().getComponentType(); // this would also cover the String array we had previously
                            } else {
                                throw new SparkException("bad field class: " + field.getType());
                            }
                            Object array = Array.newInstance(itemClazz, list.size());
                            field.set(result, list.toArray((Object[]) array));
                            field = null;
                        }
                        list = null;
                        break;
                    case START_OBJECT:
                        // we should construct these objects recursively

                        // the field type points us in the direction of the class to instantiate

                        
                        if (null != field) {
                            if (null != list) {
                                if (null != field.getType().getComponentType()) {
                                // we are in a list - we likely have a s at the end, which we should drop
                                list.add(readObject(field.getType().getComponentType(), parser));
                                }
                            } else {
                                // in case of PhoneNumber this would return a PhoneNumber object
                                field.set(result, readObject(field.getType(), parser));
                            }
                        } else {
                            list = null;
                        }
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
        final StringBuilder urlStringBuilder = new StringBuilder(baseUri.toString() + path);
        if (params != null) {
            urlStringBuilder.append("?");
            for (String param[] : params) {
                urlStringBuilder
                        .append(Client.this.encode(param[0]))
                        .append("=")
                        .append(Client.this.encode(param[1]))
                        .append("&");
            }
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

                Type type = field.getType();
                if (type == String.class) {
                    jsonGenerator.write(field.getName(), (String) value);
                } else if (type == Integer.class) {
                    jsonGenerator.write(field.getName(), (Integer) value);
                } else if (type == BigDecimal.class) {
                    jsonGenerator.write(field.getName(), (BigDecimal) value);
                } else if (type == Date.class) {
                    DateFormat dateFormat = new SimpleDateFormat(ISO8601_FORMAT);
                    jsonGenerator.write(field.getName(), dateFormat.format(value));
                } else if (type == URI.class) {
                    jsonGenerator.write(field.getName(), value.toString());
                } else if (type == JsonArray.class) {
                    jsonGenerator.write(field.getName(), (JsonArray) value);
                } else if (type == Boolean.class) {
                    jsonGenerator.write(field.getName(), (Boolean) value);
                } else if (type == String[].class) {
                    jsonGenerator.writeStartArray(field.getName());
                    for (String st : (String[]) value) {
                        jsonGenerator.write(st);
                    }
                    jsonGenerator.writeEnd();
                } else if (type == URI[].class) {
                    jsonGenerator.writeStartArray(field.getName());
                    for (URI uri : (URI[]) value) {
                        jsonGenerator.write(uri.toString());
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

    private class PagingIterator<T> implements Iterator<T> {
        private final Class<T> clazz;
        private URL url;
        private HttpsURLConnection connection;
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
                        HttpsURLConnection next = getLink(connection, "next");
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

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }



    private void scrollToItemsArray(JsonParser parser) {
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
    }



    private static final Pattern linkPattern = Pattern.compile("\\s*<(\\S+)>\\s*;\\s*rel=\"(\\S+)\",?");

    private HttpsURLConnection getLink(HttpsURLConnection connection, String rel) throws IOException {
        String link = connection.getHeaderField("Link");
        return parseLinkHeader(link, rel);
    }

    private HttpsURLConnection parseLinkHeader(String link, String desiredRel) throws IOException {
        HttpsURLConnection result = null;
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
