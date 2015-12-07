package com.ciscospark;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
class AccessTokenResponse {
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private Integer refresh_token_expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Integer getRefresh_token_expires_in() {
        return refresh_token_expires_in;
    }

    public void setRefresh_token_expires_in(Integer refresh_token_expires_in) {
        this.refresh_token_expires_in = refresh_token_expires_in;
    }
}
