package com.haifan.common.core.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageInfo;
import com.haifan.common.core.domin.R;
import com.haifan.common.core.domin.TableDataInfo;

import java.util.List;

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

    public TableDataInfo getTableDataInfo(List<?> list) {
        if (CollectionUtil.isEmpty(list)) {
            return TableDataInfo.empty();
        }
        long total = new PageInfo(list).getTotal(); // 获取符合查询条件的数据的总数
        return TableDataInfo.success(list, total);
    }
}
