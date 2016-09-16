package com.ciscospark;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

abstract public class KmsMessage {

    protected Object body;
    protected JWK messageKey;

    protected KmsMessage(Object body) {
        this.body = body;
    }

    public String asEncryptedBlob() {
        String blob = null;
        try {
            JWEHeader header = null;
            JWEEncrypter encrypter = null;
            if (messageKey instanceof RSAKey) {
                RSAPublicKey pubKey = ((RSAKey) messageKey).toRSAPublicKey();
                if (null != pubKey) {
                    header = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP, EncryptionMethod.A256GCM)
                            .keyID(messageKey.getKeyID()).build();
                    encrypter = new RSAEncrypter(pubKey);
                }
            } else if (messageKey instanceof com.nimbusds.jose.jwk.OctetSequenceKey) {
                header = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
                        .keyID(messageKey.getKeyID()).build();
                encrypter = new DirectEncrypter(((com.nimbusds.jose.jwk.OctetSequenceKey) messageKey).toByteArray());
            }
            if ((null != encrypter) && (null != header)) {
                Payload payload = new Payload(KmsApi.convertToJson(body));
                JWEObject jwe = new JWEObject(header, payload);
                jwe.encrypt(encrypter);
                blob = jwe.serialize();
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (Exception e) {
        }
        return blob;
    }

    public String asEncryptedBlob(JWK key) {
        if ((null != messageKey) && (!key.getKeyID().equals(messageKey.getKeyID()))) {
        }

        messageKey = key;
        return asEncryptedBlob();
    }
}
