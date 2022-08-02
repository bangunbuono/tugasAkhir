package com.example.fixinventori.Chat.Model;

import java.io.Serializable;

public class UserModel implements Serializable {

    public String name, email, token, image, id;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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
