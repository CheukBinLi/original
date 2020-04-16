package com.cheuks.bin.original.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = -5367065879692865023L;

    public interface AccountStatus {
        int NORMAL = 0;
        int DISABLE = 1;// 停用
        int LOCKED = 2;// 锁定
    }

    private String unid;
    private String userName;
    private String password;
    private String tenant;
    private int status;
    private long expired;
    private String source;// 来源

    private Role roles;

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setTenant(String tenant) {
        this.tenant = tenant;
        return this;
    }

    public User setStatus(int status) {
        this.status = status;
        return this;
    }

    public User setExpired(long expired) {
        this.expired = expired;
        return this;
    }

    public User setRoles(Role roles) {
        this.roles = roles;
        return this;
    }

    public User setUnid(String unid) {
        this.unid = unid;
        return this;
    }

    public User setSource(String source) {
        this.source = source;
        return this;
    }

    public User(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
    }

}
