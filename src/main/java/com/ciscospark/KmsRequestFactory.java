package com.ciscospark;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class KmsRequestFactory {

    public static KmsRequest newCreateResourceRequest(
            KmsRequestBody.Client client, String requestId, List<String> userIds, List<URI> keyUris) {
        KmsRequest request = null;
        try {
            KmsRequestBody body = new KmsRequestBody(client, KmsRequestBody.Method.create, new URI("/resources"), requestId);
            body.setUserIds(userIds);
            body.setKeyUris(keyUris);
            request = new KmsRequest(body);
        } catch (URISyntaxException e) {
        }
        return request;
    }
}
