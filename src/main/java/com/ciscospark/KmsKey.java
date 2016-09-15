package com.ciscospark;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

public class KmsKey {

    private Date bindDate;
    private String clientId;
    private Date createDate;
    private Date expirationDate;
    private KmsJwk jwk;
    private String orgId;
    private URI resourceUri;
    private URI uri;
    private String userId;

    public KmsJwk getJwk() {
        return jwk;
    }

    public void setJwk(KmsJwk jwk) {
        this.jwk = jwk;
    }

    public URI getUri() {
        return this.uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public URI getResourceUri() {
        return this.resourceUri;
    }

    public void setResourceUri(URI resourceUri) {
        this.resourceUri = resourceUri;
    }

    public Date getBindDate() {
        return this.bindDate;
    }

    public void setBindDate(Date bindDate) {
        this.bindDate = bindDate;
    }

    public URI getKmsKeyUriFromEncryptionKeyUri() {
        return URI.create("/keys/" + getUuidFromUri(uri));
    }

    private UUID getUuidFromUri(URI url) {
        String path = url.getPath();
        String[] strArray = path.split("/");
        if (strArray != null && strArray.length != 0) {
            try {
                return UUID.fromString(strArray[strArray.length - 1]);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }
}
