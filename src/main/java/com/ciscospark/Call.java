package com.ciscospark;

import java.util.Date;

/**
 * Copyright (c) 2016 Cisco Systems, Inc. See LICENSE file.
 */
public class Call {
    private String id;
    private String roomId;
    private String status;
    private Integer duration;
    private String hostOrgId;
    private String hostId;
    private Date created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getHostOrgId() {
        return hostOrgId;
    }

    public void setHostOrgId(String hostOrgId) {
        this.hostOrgId = hostOrgId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }

}
