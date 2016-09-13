package com.ciscospark;

import com.cisco.wx2.sdk.kms.KmsRequest;
import com.cisco.wx2.sdk.kms.KmsRequestBody;
import com.cisco.wx2.sdk.kms.KmsRequestFactory;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.util.Base64URL;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RequestBuilderRoomEncryptionImpl<T> extends RequestBuilderImpl<T> {

    private KeyManager keyManager;

    public RequestBuilderRoomEncryptionImpl(Class clazz, Client client, String path, KeyManager keyManager) {
        super(clazz, client, path);
        this.keyManager = keyManager;
    }

    // TODO - shouldn't pass in userUuid
    public T post(T body, UUID userUuid) {
        OctetSequenceKey hydraOctetSequenceKey = keyManager.getEcdheKey();
        KmsKey convKey = keyManager.requestNewKmsKey();
        KmsKey convTitleKey = keyManager.requestNewKmsKey();
        List<URI> keyUriList = new ArrayList<>();
        keyUriList.add(convKey.getKmsKeyUriFromEncryptionKeyUri());
        keyUriList.add(convTitleKey.getKmsKeyUriFromEncryptionKeyUri());

        // Compose Kms Message
        final List<String> auths = Arrays.asList();

        // TODO - don't use KMS SDK
        KmsRequestBody.Client kmsRequestBodyClient = new KmsRequestBody.Client() {{
            // TODO - what clientId I should use
            setClientId("https://ciscospark.com/webhookDevices/f5a0215c-d5d4-11e5-ab30-625662870761");
            getCredential().setUserId(userUuid.toString());
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

        KmsRequestFactory kmsRequestFactory = KmsRequestFactory.getInstance();
        KmsRequest requestClientSide = kmsRequestFactory.newCreateResourceRequest(kmsRequestBodyClient, requestId, auths, keyUriList);
        String requestBlob = requestClientSide.asEncryptedBlob(octetSequenceKey);
//        String requestBlob = apiHelper.constructEncryptedBlobForKeyResourceBinding(kmsRequestBodyClient, requestId, keyUriList, auths, ecdheKey);

        // TODO - not safe
        final String title = ((Room) body).getTitle();
        String encryptedTitle = encryptMessage(convTitleKey, title);
//        final KMSMessageSynchronusClient kmsMessageSynchronusClient = new KMSMessageSynchronusClient(null);
//        final String encryptedTitle = kmsMessageSynchronusClient.encryptMessage(convTitleKey, title);
        assert encryptedTitle != null;

        // Create room
        Room hydraRoomRequest = (Room) body;
        hydraRoomRequest.setTitle(encryptedTitle);
        hydraRoomRequest.setEncryptionKeyUrl(convKey.getUri().toString());
        hydraRoomRequest.setEncryptionKeyConvTitleUrl(convTitleKey.getUri().toString());
        hydraRoomRequest.setKmsMessage(requestBlob);

        // TODO - set request header!!!

        // TODO - handle response and response header
//        if (url != null) {
//            return kmsRequestBodyClient.post(clazz, url, body);
//        } else {

        return super.client.post(clazz, pathBuilder.toString(), body);
//        }
    }

    public String encryptMessage(KmsKey kmsKey, String cleartext) {
        String ciphertext = null;

        try {
            com.nimbusds.jose.jwk.OctetSequenceKey e = (com.nimbusds.jose.jwk.OctetSequenceKey)kmsKey.getJwk().toJWK();
            JWEHeader header = (new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)).keyID(e.getKeyID()).build();
            JWEObject jwe = new JWEObject(header, new Payload(cleartext));
            DirectEncrypter encrypter = new DirectEncrypter(e.toByteArray());
            jwe.encrypt(encrypter);
            ciphertext = jwe.serialize();
        } catch (Exception var8) {
//            log.info("KmsProxyHandlerService.encryptMessage - exception encountered", var8);
        }

        return ciphertext;
    }

}
