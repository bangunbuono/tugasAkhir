package com.example.fixinventori.model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    private String user;
    @SerializedName("profil_picture") private String profilImage;

    public String getUser() {
        return user;
    }

    public String getProfilImage() {
        return profilImage;
    }
}
