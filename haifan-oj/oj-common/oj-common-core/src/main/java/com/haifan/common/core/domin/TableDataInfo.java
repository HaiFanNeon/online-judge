package com.haifan.common.core.domin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.haifan.common.core.enums.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格数据信息通用封装类
 * 用于封装分页查询结果，包含数据列表、总数、状态码和消息
 */
@Data
public class TableDataInfo {

    /**
     * 数据总数
     */
    private long total;

    /**
     * 数据列表
     */
    private List<?> rows;

    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 默认构造函数
     */
    public TableDataInfo() {}

    /**
     * 返回一个空数据的 TableDataInfo 实例
     *
     * @return TableDataInfo 实例，数据列表为空，总数为 0，状态码和消息为成功
     */
    public static TableDataInfo empty() {
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(ResultCode.SUCCESS.getCode());
        tableDataInfo.setMsg(ResultCode.SUCCESS.getMsg());
        tableDataInfo.setRows(new ArrayList<>());
        tableDataInfo.setTotal(0);
        return tableDataInfo;
    }

    /**
     * 返回一个成功的 TableDataInfo 实例
     *
     * @param list  数据列表
     * @param total 数据总数
     * @return TableDataInfo 实例，包含传入的数据列表和总数，状态码和消息为成功
     */
    public static TableDataInfo success(List<?> list, long total) {
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(ResultCode.SUCCESS.getCode());
        tableDataInfo.setRows(list);
        tableDataInfo.setMsg(ResultCode.SUCCESS.getMsg());
        tableDataInfo.setTotal(total);
        return tableDataInfo;
    }
}