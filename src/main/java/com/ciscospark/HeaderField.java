package com.ciscospark;

import java.util.List;

public class HeaderField {

    private String headerName;
    private List<String> headerValue;

    public HeaderField(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }

    public List<String> getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(List<String> headerValue) {
        this.headerValue = headerValue;
    }
}
