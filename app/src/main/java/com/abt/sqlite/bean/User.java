package com.abt.sqlite.bean;

/**
 * @描述： @User
 * @作者： @黄卫旗
 * @创建时间： @2018/5/16
 */
public class User {

    public String getUsername() {
        return username;
    }

    public String getPasswd() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswd(String passwd) {
        this.password = passwd;
    }

    public String username;
    public String password;

}
