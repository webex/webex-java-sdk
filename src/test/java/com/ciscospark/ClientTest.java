package com.ciscospark;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.*;

@Test
public class ClientTest {

    @BeforeClass
    public void beforeClass() {
        MockitoAnnotations.initMocks(this);
    }

    public void testJsonParser() {
        String data = "{\"status\":201,\"requestId\":\"01b5837e-e184-4f79-af36-7a025ae02e6b\",\"keys\":[{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/3118e4a3-013d-40a9-9379-8da430af4ef4\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/3118e4a3-013d-40a9-9379-8da430af4ef4\",\"k\":\"FJJn8JrTrK6o0Q9MOP_emy4Ebqa_QJ5iWhx_8GdjObA\"},\"userId\":\"3c03e9ef-c9b0-4168-a1c3-596fdf1ce33d\",\"clientId\":\"3c03e9ef-c9b0-4168-a1c3-596fdf1ce33d\",\"createDate\":\"2016-09-14T20:47:50.822Z\",\"expirationDate\":\"2016-09-19T20:47:50.822Z\"}]}";
        Client client = spy(new Client(null, null, null, null, null, null, null, null, null));
        OctetSequenceKey octetSequenceKey = new OctetSequenceKey();
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        doReturn(inputStream).when(client).request(any(), anyString(), any(), anyString());

        KmsResponseBody kmsResponseBody = client.posts(KmsResponseBody.class, "/kms", octetSequenceKey);
        assertNotNull(kmsResponseBody);
        assertEquals(kmsResponseBody.getStatus(), new Integer(201));
        assertEquals(kmsResponseBody.getRequestId(), "01b5837e-e184-4f79-af36-7a025ae02e6b");
        assertNull(kmsResponseBody.getReason());
        assertEquals(kmsResponseBody.getKeys().length, 1);

        KmsKey kmsKey = kmsResponseBody.getKeys()[0];
        assertNotNull(kmsKey);
        assertNotNull(kmsKey.getClientId());
        assertNotNull(kmsKey.getCreateDate());
        assertNotNull(kmsKey.getExpirationDate());
        assertNotNull(kmsKey.getUri());
        assertNotNull(kmsKey.getUserId());
        assertNotNull(kmsKey.getJwk());
        assertNotNull(kmsKey.getJwk().getKid());
    }

    public void testJsonParserWithMoreItems() {
        String data = "{\"status\":201,\"requestId\":\"35e61ff0-8948-4995-ab23-f2f2f409d2a4\",\"keys\":[{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/4d354b2a-96a6-45cb-991e-829dc92cb05e\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/4d354b2a-96a6-45cb-991e-829dc92cb05e\",\"k\":\"PwvFTIyfd0298dV_e8bXEZ6um1VQbldrABeGhEitzOk\"},\"userId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"clientId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"createDate\":\"2016-09-14T21:15:25.686Z\",\"expirationDate\":\"2016-09-19T21:15:25.686Z\"},{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/c0cb8af7-8915-419d-be07-bf5caa484a4d\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/c0cb8af7-8915-419d-be07-bf5caa484a4d\",\"k\":\"rWBo6adpLtGXiMDkNYy93b2c26Q5cpuiiq84YHCEz08\"},\"userId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"clientId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"createDate\":\"2016-09-14T21:15:25.686Z\",\"expirationDate\":\"2016-09-19T21:15:25.686Z\"},{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/9a797777-fbc9-4d92-83ec-350b758e8a5a\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/9a797777-fbc9-4d92-83ec-350b758e8a5a\",\"k\":\"fgXKBW90sM5pdKEHX2lhveq54iX1dWPO_NgcJts_Cek\"},\"userId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"clientId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"createDate\":\"2016-09-14T21:15:25.686Z\",\"expirationDate\":\"2016-09-19T21:15:25.686Z\"},{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/0a90140d-3a62-4aaf-ba92-732a312bd22f\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/0a90140d-3a62-4aaf-ba92-732a312bd22f\",\"k\":\"xDeqsvETIjT4Ca5Ul1uLcw-C1CGTcHTCHaNX7WVHh4w\"},\"userId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"clientId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"createDate\":\"2016-09-14T21:15:25.686Z\",\"expirationDate\":\"2016-09-19T21:15:25.686Z\"},{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/580d284f-e18b-4ed2-8847-b8ff2ace1035\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/580d284f-e18b-4ed2-8847-b8ff2ace1035\",\"k\":\"NSuebsW8uVAb45UIQFfH9cY2xJ0Txb_hDfvIF9BDdvE\"},\"userId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"clientId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"createDate\":\"2016-09-14T21:15:25.686Z\",\"expirationDate\":\"2016-09-19T21:15:25.686Z\"},{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/a60217c7-292e-406f-a37d-b3d217f56d37\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/a60217c7-292e-406f-a37d-b3d217f56d37\",\"k\":\"oaTtz34FRhLkiUXBu8MUDo7btsapE94JLzTc-__DyoM\"},\"userId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"clientId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"createDate\":\"2016-09-14T21:15:25.686Z\",\"expirationDate\":\"2016-09-19T21:15:25.686Z\"},{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/51471aaf-4552-4fc2-b506-942e4d6fed2d\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/51471aaf-4552-4fc2-b506-942e4d6fed2d\",\"k\":\"-50XcDLZoIr8OUefVnxczhQTnQEcApkDj9jGTUng5QE\"},\"userId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"clientId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"createDate\":\"2016-09-14T21:15:25.686Z\",\"expirationDate\":\"2016-09-19T21:15:25.686Z\"},{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/489c8e80-8383-430f-8878-f50ddcab72c1\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/489c8e80-8383-430f-8878-f50ddcab72c1\",\"k\":\"09EcL4LMtW24yJrL7ksrEnB6-5Jles2cibxVeE8OXQE\"},\"userId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"clientId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"createDate\":\"2016-09-14T21:15:25.686Z\",\"expirationDate\":\"2016-09-19T21:15:25.686Z\"},{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/88efee0d-0e64-4f99-a7b7-a2fe29cc72b1\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/88efee0d-0e64-4f99-a7b7-a2fe29cc72b1\",\"k\":\"5I7tvCqaiGU7lTWDVNeuX42BGHG7eHfkKvoGgTpcU98\"},\"userId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"clientId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"createDate\":\"2016-09-14T21:15:25.686Z\",\"expirationDate\":\"2016-09-19T21:15:25.686Z\"},{\"uri\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/e8c8d2cf-afb3-496c-b4b3-9c161b197197\",\"jwk\":{\"kty\":\"oct\",\"kid\":\"http://localhost:8080/encryption-mount/encryption/api/v1/keys/e8c8d2cf-afb3-496c-b4b3-9c161b197197\",\"k\":\"XHQfmEeJL7tRewzmS_IXjV33RB-S4ZGellzJc6Z2SbI\"},\"userId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"clientId\":\"41af325b-8d5e-4344-a327-b3da2426edad\",\"createDate\":\"2016-09-14T21:15:25.686Z\",\"expirationDate\":\"2016-09-19T21:15:25.686Z\"}]}";
        Client client = spy(new Client(null, null, null, null, null, null, null, null, null));
        OctetSequenceKey octetSequenceKey = new OctetSequenceKey();
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        doReturn(inputStream).when(client).request(any(), anyString(), any(), anyString());

        KmsResponseBody kmsResponseBody = client.posts(KmsResponseBody.class, "/kms", octetSequenceKey);
        assertNotNull(kmsResponseBody);
        assertEquals(kmsResponseBody.getKeys().length, 10);
        Arrays.asList(kmsResponseBody.getKeys()).stream().forEach(kmsKey -> assertNotNull(kmsKey.getJwk()));
    }
}
