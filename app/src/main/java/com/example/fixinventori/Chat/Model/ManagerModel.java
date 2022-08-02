package com.example.fixinventori.Chat.Model;

import java.io.Serializable;

public class ManagerModel implements Serializable {

    public String username;
    public String token;
    public String image;
    public String id;

    public String getManager() {
        return manager_name;
    }

    public String manager_name;

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }
}
