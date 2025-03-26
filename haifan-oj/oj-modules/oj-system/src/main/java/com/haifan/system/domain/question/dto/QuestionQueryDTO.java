package com.haifan.system.domain.question.dto;


import com.haifan.common.core.domin.dto.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "查询对象")
public class QuestionQueryDTO extends PageQueryDTO {

    @Schema(name = "题目难度")
    private Integer difficulty;

    @Schema(name = "题目标题")
    private String title;

    private String excludeIdStr;

    @Schema(name = "题目列表")
    private Set<Long> excludeIdSet;
}
