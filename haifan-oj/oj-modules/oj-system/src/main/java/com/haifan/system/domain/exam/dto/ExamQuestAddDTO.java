package com.haifan.system.domain.exam.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
public class ExamQuestAddDTO {

    @Schema(name = "竞赛id")
    private Long examId;

    @Schema(name = "questionId")
    private Set<Long> questionIdSet;
}
