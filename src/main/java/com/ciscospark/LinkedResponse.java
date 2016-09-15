package com.ciscospark;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkedResponse<T> {
    private static final Pattern linkPattern = Pattern.compile("\\s*<(\\S+)>\\s*;\\s*rel=\"(\\S+)\",?");
    private final Client client;
    private final Function<InputStream, T> bodyCreator;
    private final Map<String, URL> urls = new LinkedHashMap<>();
    private Response response;

    public LinkedResponse(Client client, URL url, Function<InputStream, T> bodyCreator) throws IOException {
        this.client = client;
        this.bodyCreator = bodyCreator;
        followUrl(url);
    }

    public Collection<String> getLinks() {
        return urls.keySet();
    }

    public boolean hasLink(String rel) {
        return urls.containsKey(rel);
    }

    public T consumeBody() {
        return bodyCreator.apply(response.inputStream);
    }

    public void followLink(String rel) {
        if (hasLink(rel)) {
            followUrl(urls.get(rel));
        } else {
            throw new NoSuchElementException();
        }
    }

    public URL getLink(String rel) {
        return urls.get(rel);
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

    private void parseLinks(HttpURLConnection connection) throws IOException {
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

    private boolean isOk(int responseCode) {
        return (responseCode >= 200 && responseCode < 400);
    }
}
