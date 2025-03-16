package com.haifan.common.core.domin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity {
//    @ApiModelProperty(value = "创建人")
    private Long createBy;
//    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
//    @ApiModelProperty(value = "更新人")
    private Long updateBy;
//    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
