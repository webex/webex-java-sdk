package com.ciscospark;

import java.util.Date;

/**
 * Copyright (c) 2016 Cisco Systems, Inc. See LICENSE file.
 */
public class License {
    private String id;
    private String displayName;
    private String name;
    private Integer totalUnits;
    private Integer consumedUnits;    


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(Integer totalUnits) {
        this.totalUnits = totalUnits;
    }

    public Integer getConsumedUnits() {
        return consumedUnits;
    }

    public void setConsumedUnits(Integer consumedUnits) {
        this.consumedUnits = consumedUnits;
    }

    
}
