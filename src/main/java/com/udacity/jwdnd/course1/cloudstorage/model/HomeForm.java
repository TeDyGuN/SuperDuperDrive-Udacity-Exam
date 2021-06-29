package com.udacity.jwdnd.course1.cloudstorage.model;

public class HomeForm {

    private String title;
    private String description;
    private int type;
    private String username;
    private String key;
    private String password;

    public HomeForm(String title, String description, int type, String username, String key, String password) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.username = username;
        this.key = key;
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
