package com.ciscospark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KmsKeyManager {

    private final static Integer KMS_KEY_REQUEST_COUNT = 10;
    private final Client client;
    private OctetSequenceKey ecdheKey;
    private List<KmsKey> kmsKeyList = new ArrayList<>();
    private KmsInfo publicKey;

    // TODO - store the keys here, if run out, request more

    // TODO - cache keys here, set the max number of keys and drop the oldest key
    // FIFO - first in first out

    public KmsKeyManager(Client client) {
        this.client = client;
    }

    public KmsKey requestNewKmsKey() {
        // TODO - handle exceptions
        if (kmsKeyList.isEmpty()) {
            KmsResponseBody kmsResponseBody = createKeys(getEcdheKey(), KMS_KEY_REQUEST_COUNT);
            if (kmsResponseBody.getStatus() >= 200 && kmsResponseBody.getStatus() < 300 && kmsResponseBody.getKeys() != null) {
                kmsKeyList.addAll(new ArrayList<>(Arrays.asList(kmsResponseBody.getKeys())));
            } else {
                // TODO - handle exceptions
            }
        }
        return kmsKeyList.remove(0);
    }

    public OctetSequenceKey getEcdheKey() {
        if (ecdheKey == null) {
            ecdheKey = createEcdheKey(getPublicKey());
        }
        return ecdheKey;
    }

    private KmsInfo getPublicKey() {
        if (publicKey == null) {
            publicKey = client.get(KmsInfo.class, "/kms/publicKey", null);
        }
        return publicKey;
    }

    private OctetSequenceKey createEcdheKey(KmsInfo kmsInfo) {
        return client.posts(OctetSequenceKey.class, "/kms/ecdheKey", kmsInfo);
    }

    private KmsResponseBody createKeys(OctetSequenceKey octetSequenceKey, Integer count) {
        // Pass param for keys count
        String[] param = {"count", count.toString()};
        List<String[]> params = new ArrayList<>();
        params.add(param);
        return client.posts(KmsResponseBody.class, "/kms", params, octetSequenceKey);
    }
}
