package com.ciscospark;

public class PhoneNumber {
    private String type;
    private String value;

    public PhoneNumber(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // when the class is instantiated via indirect methods like blah.newInstance()
    // it will use the empty constructor
    public PhoneNumber(){
        this.type = null;
        this.value = null;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String number) {
        this.value = number;
    }

    @Override
    public String toString() {
        return ("type: " + this.type + " number: " + this.value);
    }
}
