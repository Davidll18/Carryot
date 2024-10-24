package com.example.superadmin.model;

public class LogEntry {
    private String username;
    private String action;
    private String timestamp;

    public LogEntry(String username, String action, String timestamp) {
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

