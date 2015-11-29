package com.ciscospark;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 11/24/15.
 */
class RequestBuilderImpl<T> implements RequestBuilder<T> {
    final StringBuilder pathBuilder;
    final List<String[]> params;
    final Client client;
    final Class<T> clazz;

    RequestBuilderImpl(Class<T> clazz, Client client, String path) {
        this(clazz, client, new StringBuilder(path), new ArrayList<>());
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
    public RequestBuilder<T> path(String path) {
        this.pathBuilder.append(path);
        return this;
    }

    @Override
    public <NewType> RequestBuilder<NewType> path(String path, Class<NewType> clazz) {
        pathBuilder.append(path);
        return new RequestBuilderImpl<>(clazz, client, pathBuilder, params);
    }

    @Override
    public T post(T body) {
        return client.post(clazz, pathBuilder.toString(), body);
    }

    @Override
    public T put(T body) {
        return client.put(clazz, pathBuilder.toString(), body);
    }

    @Override
    public T get() {
        return client.get(clazz, pathBuilder.toString(), params);
    }

    @Override
    public Iterator<T> list() {
        return client.list(clazz, pathBuilder.toString(), params);
    }

    @Override
    public void delete() {
        client.delete(pathBuilder.toString());
    }
}
