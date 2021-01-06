package com.ciscospark;


import java.util.List;

public class Content {

    private HeaderField cacheControl = new HeaderField("Cache-Control");
    private HeaderField contentDisposition = new HeaderField("Content-Disposition");
    private HeaderField contentLength = new HeaderField("Content-Length");
    private HeaderField contentType = new HeaderField("Content-Type");

    public List<String> getCacheControl() {
        return cacheControl.getHeaderValue();
    }

    public List<String> getContentDisposition() {
        return contentDisposition.getHeaderValue();
    }

    public List<String> getContentLength() {
        return contentLength.getHeaderValue();
    }

    public List<String> getContentType() {
        return contentType.getHeaderValue();
    }

}
