package com.ciscospark;

import java.net.URI;
import java.util.Date;
import java.util.List;

public class KmsRequestBody {

    private Date boundAfter;
    private Date boundBefore;
    private Client client;
    private Integer count;
    private KmsJwk jwk;
    private List<URI> keyUris;
    private Method method;
    private String requestId;
    private URI resourceUri;
    private URI uri;
    private List<String> userIds;

    public KmsRequestBody() {
    }

    public KmsRequestBody(Client client, Method method, URI uri, String requestId) {
        this.client = client;
        this.method = method;
        this.uri = uri;
        this.requestId = requestId;
        this.jwk = null;
        this.userIds = null;
        this.keyUris = null;
        this.resourceUri = null;
        this.boundAfter = null;
        this.boundBefore = null;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public KmsJwk getJwk() {
        return jwk;
    }

    public void setJwk(KmsJwk jwk) {
        this.jwk = jwk;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<URI> getKeyUris() {
        return keyUris;
    }

    public void setKeyUris(List<URI> keyUris) {
        this.keyUris = keyUris;
    }

    public URI getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(URI resourceUri) {
        this.resourceUri = resourceUri;
    }

    public Date getBoundAfter() {
        return boundAfter;
    }

    public void setBoundAfter(Date boundAfter) {
        this.boundAfter = boundAfter;
    }

    public Date getBoundBefore() {
        return boundBefore;
    }

    public void setBoundBefore(Date boundBefore) {
        this.boundBefore = boundBefore;
    }

    public enum Method {
        create,
        retrieve,
        update,
        delete
    }

    public static class Client {
        private String clientId;
        private Credential credential;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public Credential getCredential() {
            if (null == credential) {
                credential = new Credential();
            }
            return credential;
        }

        public void setCredential(Credential credential) {
            this.credential = credential;
        }
    }

    public static class Credential {
        private String bearer;
        private String cert;
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getBearer() {
            return bearer;
        }

        public void setBearer(String bearer) {
            this.bearer = bearer;
        }

        public String getCert() {
            return cert;
        }

        public void setCert(String cert) {
            this.cert = cert;
        }
    }
}
