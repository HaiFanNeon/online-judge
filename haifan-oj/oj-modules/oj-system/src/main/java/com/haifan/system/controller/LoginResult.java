package com.haifan.system.controller;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResult {

    private int code;
    private String msg;
}
