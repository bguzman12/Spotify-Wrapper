package com.example.cs2340project2.ui.login;

public class Firebase {
    private String email, password, token;

    public Firebase() {
    }

    public Firebase(String username, String password, String token) {
        this.email = username;
        this.password = password;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
