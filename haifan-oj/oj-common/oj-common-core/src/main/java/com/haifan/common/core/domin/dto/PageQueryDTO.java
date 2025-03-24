package com.haifan.common.core.domin.dto;

import lombok.Data;

@Data
public class PageQueryDTO {

    private Integer pageSize = 10;

    private Integer pageNum = 1;
}
