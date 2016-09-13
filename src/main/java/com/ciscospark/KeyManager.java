package com.ciscospark;

import com.cisco.wx2.hydra.client.HydraClientFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyManager {

    private final static Integer KMS_KEY_REQUEST_COUNT = 10;
    private final Client client;
    private KmsInfo publicKey;
    private OctetSequenceKey ecdheKey;
    private List<KmsKey> kmsKeyList = new ArrayList<>();

    // TODO - store the keys here, if run out, request more

    // TODO - cache keys here, set the max number of keys and drop the oldest key
    // FIFO - first in first out

    public KeyManager(Client client) {
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

    private KmsResponseBody createKeys(OctetSequenceKey octetSequenceKey, int count) {
        // Pass param for keys count
//        return client.posts(KmsResponseBody.class, "/kms", octetSequenceKey);

        HydraClientFactory hydraClientFactory = HydraClientFactory.builder()
                .baseUrl("http://localhost:8080/hydra-mount/v1")
                .apiServiceUrl(URI.create("http://localhost:8080/hydra-mount/v1"))
                .connectTimeout(30 * 1000)
                .readTimeout(60 * 1000)
                .userAgent("Spark Hydra Testing")
                .disableSSLChecks(true)
                .maxConnections(100)
                .maxConnectionsPerRoute(100)
                .build();
        com.cisco.wx2.client.Client hydraClient = hydraClientFactory.newHydraClient(client.getAccessToken());
        return hydraClient.post("kms")
                .jsonEntity(octetSequenceKey)
                .param("count", count)
                .execute(KmsResponseBody.class);
    }
}
