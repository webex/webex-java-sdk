package com.ciscospark;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.jwk.KeyOperation;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jose.util.Base64URL;

import java.net.URI;
import java.util.List;
import java.util.Set;

public class OctetSequenceKey {

    private Algorithm alg;
    private String k;
    private String kid;
    private Set<KeyOperation> ops;
    private KeyUse use;
    private List<Base64> x5c;
    private Base64URL x5t;
    private URI x5u;

    public Algorithm getAlg() {
        return alg;
    }

    public void setAlg(Algorithm alg) {
        this.alg = alg;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public Set<KeyOperation> getOps() {
        return ops;
    }

    public void setOps(Set<KeyOperation> ops) {
        this.ops = ops;
    }

    public KeyUse getUse() {
        return use;
    }

    public void setUse(KeyUse use) {
        this.use = use;
    }

    public List<Base64> getX5c() {
        return x5c;
    }

    public void setX5c(List<Base64> x5c) {
        this.x5c = x5c;
    }

    public Base64URL getX5t() {
        return x5t;
    }

    public void setX5t(Base64URL x5t) {
        this.x5t = x5t;
    }

    public URI getX5u() {
        return x5u;
    }

    public void setX5u(URI x5u) {
        this.x5u = x5u;
    }
}
