package com.haifan.system.domain.user.vo;


import lombok.Data;


@Data
public class UserVO {

    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户状态1: 男  2：女
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * 学校
     */
    private String schoolName;

    /**
     * 专业
     */
    private String majorName;

    /**
     * 个人介绍
     */
    private String introduce;

    /**
     * 用户状态0: 拉黑  1：正常
     */
    private Integer status;

}
