package com.ciscospark;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
class RequestBuilderImpl<T> implements RequestBuilder<T> {
    private URL url;
    final StringBuilder pathBuilder;
    final List<String[]> params;
    final Client client;
    final Class<T> clazz;

    RequestBuilderImpl(Class<T> clazz, Client client, String path) {
        this(clazz, client, new StringBuilder(path), new ArrayList<String[]>());
    }

    private RequestBuilderImpl(Class<T> clazz, Client client, StringBuilder pathBuilder, List<String[]> params) {
        this.clazz = clazz;
        this.client = client;
        this.pathBuilder = pathBuilder;
        this.params = params;
    }

    @Override
    public RequestBuilder<T> queryParam(String key, String value) {
        params.add(new String[] { key, value });
        return this;
    }

    @Override
    public RequestBuilder<T> path(Object... paths) {
        for (Object path : paths) {
            this.pathBuilder.append(path);
        }
        return this;
    }

    @Override
    public <NewType> RequestBuilder<NewType> path(String path, Class<NewType> clazz) {
        pathBuilder.append(path);
        return new RequestBuilderImpl<NewType>(clazz, client, pathBuilder, params);
    }

    @Override
    public RequestBuilder<T> url(URL url) {
        this.url = url;
        return this;
    }

    @Override
    public T post(T body) {
        if (url != null) {
            return client.post(clazz, url, body);
        } else {
            return client.post(clazz, pathBuilder.toString(), body);
        }
    }

    @Override
    public T put(T body) {
        if (url != null) {
            return client.put(clazz, url, body);
        } else {
            return client.put(clazz, pathBuilder.toString(), body);
        }
    }

    @Override
    public T get() {
        if (url != null) {
            return client.get(clazz, url);
        } else {
            return client.get(clazz, pathBuilder.toString(), params);
        }
    }

    @Override
    public Iterator<T> iterate() {
        if (url != null) {
            return client.list(clazz, url);
        } else {
            return client.list(clazz, pathBuilder.toString(), params);
        }
    }

    @Override
    public LinkedResponse<List<T>> paginate() {
        if (url != null) {
            return client.paginate(clazz, url);
        } else {
            return client.paginate(clazz, pathBuilder.toString(), params);
        }
    }

    @Override
    public void delete() {
        if (url != null) {
            client.delete(url);
        } else {
            client.delete(pathBuilder.toString());
        }
    }
}
