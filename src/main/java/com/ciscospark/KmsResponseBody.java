package com.ciscospark;

public class KmsResponseBody {

    private Integer status;
    private String requestId;
    private String reason;
    private KmsKey[] keys;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public KmsKey[] getKeys() {
        return keys;
    }

    public void setKeys(KmsKey[] keys) {
        this.keys = keys;
    }
}
