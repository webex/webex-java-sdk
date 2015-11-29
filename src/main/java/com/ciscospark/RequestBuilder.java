package com.ciscospark;

import java.util.Iterator;

/**
 * Created on 11/24/15.
 */
public interface RequestBuilder<T> {
    RequestBuilder<T> queryParam(String key, String value);
    RequestBuilder<T> path(String path);
    <NewType> RequestBuilder<NewType> path(String path, Class<NewType> clazz);
    T post(T body);
    T put(T body);
    T get();
    Iterator<T> list();
    void delete();
}
