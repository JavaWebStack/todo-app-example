package org.javawebstack.todoexample.request;

import lombok.Getter;
import org.javawebstack.validator.Rule;

@Getter
public class LoginRequest {

    @Rule("required")
    String username;
    @Rule("required")
    String password;

}
