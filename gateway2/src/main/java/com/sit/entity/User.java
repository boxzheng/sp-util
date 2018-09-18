package com.sit.entity;

import java.util.List;

/**
 * Created by zhenglin on 2018/9/13.
 */
public class User {
    private Integer id;

    private String account;

    private String password;

    private String username;

    private String status;

    private String tel;
    private List<String> roles;

    public User(User in)
    {
        this.id = in.getId();
        this.account = in.getAccount();
        this.password = in.getPassword();
        this.username = in.getUsername();
        this.status = in.getStatus();
        this.tel = in.getTel();
        this.roles.addAll(in.getRoles());
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
