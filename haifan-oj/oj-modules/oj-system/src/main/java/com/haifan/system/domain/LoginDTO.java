package com.haifan.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginDTO {
    @Schema(description = "用户账号")
    private String userAccount;
    @Schema(description = "用户密码")
    private String password;
}
