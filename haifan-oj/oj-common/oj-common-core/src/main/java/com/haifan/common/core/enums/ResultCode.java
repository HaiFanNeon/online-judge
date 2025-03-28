package com.haifan.common.core.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ResultCode {
/** 定义状态码 */
//操作唱功
    SUCCESS                             (1000, "操作成功"),
    //服务器内部错误，友好提示
    ERROR                               (2000, "服务繁忙请稍后重试"),
    //操作失败，但是服务器不存在异常
    FAILED                              (3000, "操作失败"),
    FAILED_UNAUTHORIZED                 (3001, "未授权"),
    FAILED_PARAMS_VALIDATE              (3002, "参数校验失败"),
    FAILED_NOT_EXISTS                   (3003, "资源不存在"),
    FAILED_ALREADY_EXISTS               (3004, "资源已存在"),
    AILED_USER_EXISTS                   (3101, "用户已存在"),
    FAILED_USER_NOT_EXISTS              (3102, "用户不存在"),
    FAILED_LOGIN                        (3103, "用户名或密码错误"),
    FAILED_USER_BANNED                  (3104, "您已被列入黑名单, 请联系管理员."),
    FAILED_USER_PHONE                   (3105, "您输入的手机号有误"),
    EXAM_QUESTION_NOT_EXISTS            (4000, "竞赛中的题目不存在"),
    EXAM_QUESTION_IS_NULL               (4001, "竞赛中的题目为空"),
    EXAM_START                          (4002, "竞赛已经发布，不能修改题目");


    /**
     * 状态码
     */
    private int code;
    /**
     * 状态描述
     */
    private String msg;
}