package com.haifan.system.domain.question.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class QuestionAddDTO {

    @Schema(name = "题目标题")
    @NotNull
    private String title;

    @Schema(name = "题目难度1:简单  2：中等 3：困难")
    @NotNull
    private Integer difficulty;

    @NotNull
    @Schema(name = "时间限制")
    private Integer timeLimit;

    @Schema(name = "空间限制")
    @NotNull
    private Integer spaceLimit;

    @Schema(name = "题目内容")
    @NotNull
    private String content;

    @Schema(name = "题目用例")
    @NotNull
    private String questionCase;

    @Schema(name = "默认代码块")
    @NotNull
    private String defaultCode;

    @Schema(name = "main函数")
    @NotNull
    private String mainFuc;
}
