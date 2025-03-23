package com.haifan.system.domain.question.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class QuestionVO {

    @Schema(name = "题目id")
    private Long questionId;
    @Schema(name = "题目标题")
    private String title;
    @Schema(name = "题目难度1:简单  2：中等 3：困难")
    private Integer difficulty;
    @Schema(name = "创建人姓名")
    private String createName;
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
}
