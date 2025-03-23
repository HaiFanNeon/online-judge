package com.haifan.system.domain.question.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "查询对象")
public class QuestionQueryDTO {

    @Schema(name = "题目难度")
    private Integer difficulty;

    @Schema(name = "题目标题")
    private String title;

    @Schema(name = "分页大小")
    private Integer pageSize = 10;
    @Schema(name = "页数")
    private Integer pageNum = 1;
}
