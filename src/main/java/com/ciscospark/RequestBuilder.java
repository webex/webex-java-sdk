package com.ciscospark;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public interface RequestBuilder<T> {
    RequestBuilder<T> queryParam(String key, String value);
    RequestBuilder<T> path(Object... paths);
    <NewType> RequestBuilder<NewType> path(String path, Class<NewType> clazz);
    RequestBuilder<T> url(URL url);
    T post(T body);
    T put(T body);
    T get();
    Iterator<T> iterate();
    LinkedResponse<List<T>> paginate();
    void delete();
}
