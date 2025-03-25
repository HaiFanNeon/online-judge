package com.haifan.system.domain.exam.dto;


import com.haifan.common.core.domin.dto.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExanQustionQueryDTO extends PageQueryDTO {

    @Schema(name = "题目难度")
    private Integer difficulty;
    @Schema(name = "题目标题")
    private String title;

}
