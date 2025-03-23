package com.haifan.system.domain.sysuser.dto;

import com.haifan.common.core.domin.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank
    @Schema(description = "用户账号")
    private String userAccount;

    @NotBlank
    @Size(min = 6, max = 12, message = "密码长度>=6&&<=12")
    @Schema(description = "用户密码")
    private String password;
}
