package com.ciscospark;

import java.net.URI;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
class AccessTokenRequest {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String code;
    private String refresh_token;
    private URI redirect_uri;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public URI getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(URI redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
