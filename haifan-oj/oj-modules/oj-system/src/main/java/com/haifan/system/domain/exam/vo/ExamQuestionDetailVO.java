package com.haifan.system.domain.exam.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExamQuestionDetailVO {

    @Schema(name = "题目id")
    private String questionId;

    @Schema(name = "题目难度")
    private Integer difficulty;
    @Schema(name = "题目标题")
    private String title;


}
