package com.ciscospark;

public class SipAddress {
    private String type;
    private String value;
    private boolean primary;

    public SipAddress(String type, String value, boolean primary) {
        this.type = type;
        this.value = value;
        this.primary = primary;
    }

    public SipAddress() {
        this.type = null;
        this.value = null;
        this.primary = false;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public boolean getPrimary() {
        return this.primary;
    }

    @Override
    public String toString() {
        return ("type: " + this.type + " value: " + this.value + " primary: " + this.primary);
    }
}
