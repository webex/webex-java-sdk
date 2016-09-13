package com.ciscospark;

public class Room {
    private String id;
    private String title;
    private String teamId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
    }
}
