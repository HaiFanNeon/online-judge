package com.haifan.common.core.controller;


import com.haifan.common.core.domin.R;

/**
 * 返回值
 */
public class BaseController {
    public R toResult(int rows) {
        return rows > 0 ? R.ok() : R.fail();
    }

    public R toResult(Boolean ret) {
        return ret ? R.ok() : R.fail();
    }
}
