package com.haifan.system.domain.user.dto;


import com.haifan.common.core.domin.dto.PageQueryDTO;
import lombok.Data;

@Data
public class UserQueryDTO extends PageQueryDTO {

    private Long userId;

    private String nickName;

}
