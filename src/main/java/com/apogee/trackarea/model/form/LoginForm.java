package com.apogee.trackarea.model.form;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginForm {
//    @NotNull
//    @Email(message = "Email should be valid")
    private String username;
    @NotNull
    private String password;
}
