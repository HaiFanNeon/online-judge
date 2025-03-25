package com.haifan.system.domain.exam.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamVO {

    @Schema(name = "竞赛id")
    private String examId;

    @Schema(name = "竞赛标题")
    private String title;

    @Schema(name = "竞赛开始时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(name = "竞赛结束时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(name = "是否发布 0：未发布  1：已发布")
    private Integer status;
    @Schema(name = "创建人")
    private String createName;
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
}
