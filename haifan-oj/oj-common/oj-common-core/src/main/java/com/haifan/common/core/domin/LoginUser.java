package com.haifan.common.core.domin;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginUser {

    private Integer identity;
    private String nickName;
}
