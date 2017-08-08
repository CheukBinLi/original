package com.cheuks.bin.original.common.web.common.model;

import java.io.Serializable;

public class UserModel implements Serializable {

    private static final long serialVersionUID = -4740446084350417006L;

    private String user;

    private String password;

    public String getUser() {
        return user;
    }

    public UserModel setUser(String user) {
        this.user = user;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserModel setPassword(String password) {
        this.password = password;
        return this;
    }

}
