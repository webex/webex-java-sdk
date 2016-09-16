package com.ciscospark;

import java.net.URI;

public class KmsInfo {

    private URI cluster;
    private String rsaPublicKey;

    public URI getCluster() {
        return cluster;
    }

    public void setCluster(URI cluster) {
        this.cluster = cluster;
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }
}
