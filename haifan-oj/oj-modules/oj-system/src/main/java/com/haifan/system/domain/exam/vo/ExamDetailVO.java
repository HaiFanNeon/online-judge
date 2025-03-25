package com.haifan.system.domain.exam.vo;


import com.haifan.system.domain.question.vo.QuestionVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamDetailVO {
    @Schema(name = "title")
    private String title;

    @Schema(name = "竞赛开始时间")
    private LocalDateTime startTime;

    @Schema(name = "竞赛结束时间")
    private LocalDateTime endTime;

    @Schema(name = "examQuestionList")
    private List<QuestionVO>  examQuestionList;
}
