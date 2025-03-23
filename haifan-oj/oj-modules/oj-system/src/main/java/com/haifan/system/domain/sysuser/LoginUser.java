package com.haifan.system.domain.sysuser;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Schema(name = "登录对象")
@Accessors(chain = true)
public class LoginUser {

    @Schema(name = "用户身份，1为普通用户，2表示管理员")
    private Integer identity;

}
