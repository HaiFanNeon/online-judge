package com.haifan.system.domain.exam.dto;


import com.haifan.common.core.domin.dto.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ExamQueryDTO extends PageQueryDTO {

    @Schema(name = "开始时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @Schema(name = "结束时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @Schema(name = "竞赛名称")
    private String title;
}
