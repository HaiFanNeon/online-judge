package com.haifan.common.core.constants;

import lombok.Data;

@Data
public class JwtConstants {
    // jwt中载荷ployed存储的内容
    public final static String LOGIN_USER_ID = "userId";// userId 主键id
    public final static String LOGIN_USER_KEY = "userKey";// 糊涂工具包生成的uuid作为唯一标识

}
