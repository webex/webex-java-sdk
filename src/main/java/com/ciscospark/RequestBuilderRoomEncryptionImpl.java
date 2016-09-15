package com.ciscospark;

import com.nimbusds.jose.util.Base64URL;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RequestBuilderRoomEncryptionImpl<T> extends RequestBuilderImpl<T> {

    private KmsKeyManager kmsKeyManager;

    public RequestBuilderRoomEncryptionImpl(Class clazz, Client client, String path, KmsKeyManager kmsKeyManager) {
        super(clazz, client, path);
        this.kmsKeyManager = kmsKeyManager;
    }

    // TODO - shouldn't pass in userUuid
    public T post(T body) {
        OctetSequenceKey hydraOctetSequenceKey = kmsKeyManager.getEcdheKey();
        KmsKey convKey = kmsKeyManager.requestNewKmsKey();
        KmsKey convTitleKey = kmsKeyManager.requestNewKmsKey();
        List<URI> keyUriList = new ArrayList<>();
        keyUriList.add(convKey.getKmsKeyUriFromEncryptionKeyUri());
        keyUriList.add(convTitleKey.getKmsKeyUriFromEncryptionKeyUri());

        // Compose Kms Message
        final List<String> auths = Arrays.asList();

        // TODO - don't use KMS SDK
        KmsRequestBody.Client kmsRequestBodyClient = new KmsRequestBody.Client() {{
            // TODO - what clientId I should use
            setClientId("https://ciscospark.com/webhookDevices/f5a0215c-d5d4-11e5-ab30-625662870761");
//            getCredential().setUserId(userUuid.toString()); // Not necessary
            getCredential().setBearer(client.getAccessToken());
        }};
        com.nimbusds.jose.jwk.OctetSequenceKey octetSequenceKey = new com.nimbusds.jose.jwk.OctetSequenceKey.Builder(new Base64URL(hydraOctetSequenceKey.getK()))
                .keyUse(hydraOctetSequenceKey.getUse())
                .keyOperations(hydraOctetSequenceKey.getOps())
                .algorithm(hydraOctetSequenceKey.getAlg())
                .keyID(hydraOctetSequenceKey.getKid())
                .x509CertURL(hydraOctetSequenceKey.getX5u())
                .x509CertThumbprint(hydraOctetSequenceKey.getX5t())
                .x509CertChain(hydraOctetSequenceKey.getX5c())
                .build();
        String requestId = UUID.randomUUID().toString();

        KmsRequest kmsRequest = KmsRequestFactory.newCreateResourceRequest(kmsRequestBodyClient, requestId, auths, keyUriList);
        String requestBlob = kmsRequest.asEncryptedBlob(octetSequenceKey);

        // TODO - not safe, don't cast
        // Create room
        Room hydraRoomRequest = (Room) body;
        String title = hydraRoomRequest.getTitle();
        String encryptedTitle = convTitleKey.encryptMessage(title);
        hydraRoomRequest.setTitle(encryptedTitle);
        hydraRoomRequest.setEncryptionKeyUrl(convKey.getUri().toString());
        hydraRoomRequest.setTitleEncryptionKeyUrl(convTitleKey.getUri().toString());
        hydraRoomRequest.setKeyManagementMessage(requestBlob);

        // TODO - handle response and response header
        return client.post(clazz, pathBuilder.toString(), body);
    }
}
