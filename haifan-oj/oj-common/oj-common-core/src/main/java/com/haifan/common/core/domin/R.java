package com.haifan.common.core.domin;


import lombok.Data;

@Data
public class R<T> {

    private int code;

    private String msg;

    private T data;

}
