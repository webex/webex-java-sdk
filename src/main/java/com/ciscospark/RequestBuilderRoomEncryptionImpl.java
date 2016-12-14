package com.ciscospark;

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

    public T post(T body) {
        // Retrieve keys
        OctetSequenceKey octetSequenceKey = kmsKeyManager.getEcdheKey();
        KmsKey convKey = kmsKeyManager.requestNewKmsKey();
        KmsKey convTitleKey = kmsKeyManager.requestNewKmsKey();
        List<URI> keyUriList = new ArrayList<>();
        keyUriList.add(convKey.getKmsKeyUriFromEncryptionKeyUri());
        keyUriList.add(convTitleKey.getKmsKeyUriFromEncryptionKeyUri());

        // Compose KmsMessage
        final List<String> auths = Arrays.asList();
        KmsRequestBody.Client kmsRequestBodyClient = new KmsRequestBody.Client() {{
            // TODO - what clientId I should use
            setClientId("https://ciscospark.com/sdk/" + UUID.randomUUID());
//            getCredential().setUserId(userUuid.toString()); // Not necessary
            getCredential().setBearer(client.getAccessToken());
        }};
        com.nimbusds.jose.jwk.OctetSequenceKey jwkOctetSequenceKeyoctetSequenceKey = octetSequenceKey.getJwkOctetSequenceKey();
        String requestId = UUID.randomUUID().toString();
        KmsRequest kmsRequest = KmsRequestFactory.newCreateResourceRequest(kmsRequestBodyClient, requestId, auths, keyUriList);
        String requestBlob = kmsRequest.asEncryptedBlob(jwkOctetSequenceKeyoctetSequenceKey);

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
