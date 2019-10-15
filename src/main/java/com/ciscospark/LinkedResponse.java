package com.ciscospark;

import java.io.IOException;
import java.io.InputStream;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public class LinkedResponse<T> {
    private final Client client;
    private Client.Response response;
    private final BodyCreator<T> bodyCreator;
    private final Map<String,URL> urls = new LinkedHashMap<String, URL>();

    static interface BodyCreator<T> {
        T create(InputStream stream);
    }

    LinkedResponse(Client client, URL url, BodyCreator<T> bodyCreator) throws IOException {
        this.client = client;
        this.bodyCreator = bodyCreator;
        followUrl(url);
    }

    private void followUrl(URL url) {
        try {
            this.response = client.request(url, "GET", null);
            int responseCode = this.response.connection.getResponseCode();
            if (!isOk(responseCode)) {
                throw new IOException("bad response code: " + responseCode);
            }
            parseLinks(response.connection);
        } catch (IOException e) {
            throw new SparkException(e);
        }
    }

    private static final Pattern linkPattern = Pattern.compile("\\s*<(\\S+)>\\s*;\\s*rel=\"(\\S+)\",?");

    private void parseLinks(HttpsURLConnection connection) throws IOException {
        urls.clear();
        String link = connection.getHeaderField("Link");
        if (link != null && !"".equals(link)) {
            Matcher matcher = linkPattern.matcher(link);
            while (matcher.find()) {
                String url = matcher.group(1);
                String foundRel = matcher.group(2);
                urls.put(foundRel, new URL(url));
            }
        }
    }

    public Collection<String> getLinks() {
        return urls.keySet();
    }

    public boolean hasLink(String rel) {
        return urls.containsKey(rel);
    }

    public T consumeBody() {
        return bodyCreator.create(response.inputStream);
    }

    public void followLink(String rel) {
        if (hasLink(rel)) {
            followUrl(urls.get(rel));
        } else {
            throw new NoSuchElementException();
        }
    }

    private boolean isOk(int responseCode) {
        return (responseCode >= 200 && responseCode < 400);
    }

    public URL getLink(String rel) {
        return urls.get(rel);
    }
}
