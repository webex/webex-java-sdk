package com.ciscospark;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class Response {
    public HttpURLConnection connection;
    public InputStream inputStream;

    public Response(HttpURLConnection connection, InputStream inputStream) {
        this.connection = connection;
        this.inputStream = inputStream;
    }
}
