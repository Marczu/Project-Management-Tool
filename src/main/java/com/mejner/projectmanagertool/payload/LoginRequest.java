package com.mejner.projectmanagertool.payload;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Nazwa użytkownika nie może być pusta")
    private String username;
    @NotBlank(message = "Hasło nie może być puste")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
