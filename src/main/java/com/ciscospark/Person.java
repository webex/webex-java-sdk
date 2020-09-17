package com.ciscospark;

import java.util.Date;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public class Person {
    private String id;
    private String displayName;
    private String[] emails;
    private String firstName;
    private String lastName;
    private String avatar;
    private String orgId;
    private String[] roles;
    private String[] licenses;
    private Date created;
    private String timeZone;
    private Date lastActivity;
    private String status;
    private String type;
    private PhoneNumber[] phoneNumbers;
    private Boolean loginEnabled;
    private Date lastModified;
    private String nickName;
    private Boolean invitePending;
    private SipAddress[] sipAddresses;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String[] getEmails() {
        return emails;
    }

    public void setEmails(String[] emails) {
        this.emails = emails;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String[] getLicenses() {
        return licenses;
    }

    public void setLicenses(String[] licenses) {
        this.licenses = licenses;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPhoneNumbers(PhoneNumber[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public PhoneNumber[] getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public Boolean getLoginEnabled() {
        return loginEnabled;
    }

    public void setLoginEnabled(Boolean loginEnabled) {
        this.loginEnabled = loginEnabled;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getInvitePending() {
        return invitePending;
    }

    public void setInvitePending(Boolean invitePending) {
        this.invitePending = invitePending;
    }

    public void setSipAddresses(SipAddress[] sipAddresses){
        this.sipAddresses = sipAddresses;
    }

    public SipAddress[] getSipAddresses() {
        return sipAddresses;
    }
}
