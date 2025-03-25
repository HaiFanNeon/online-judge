package com.haifan.system.domain.question.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionDetailVO {

    @Schema(name = "题id")
    private String questionId;

    @Schema(name = "题目标题")
    private String title;

    @Schema(name = "题目难度1:简单  2：中等 3：困难")
    private Integer difficulty;

    @Schema(name = "时间限制")
    private Integer timeLimit;

    @Schema(name = "空间限制")
    private Integer spaceLimit;

    @Schema(name = "题目内容")
    private String content;

    @Schema(name = "题目用例")
    private String questionCase;

    @Schema(name = "默认代码块")
    private String defaultCode;

    @Schema(name = "main函数")
    private String mainFuc;
}
