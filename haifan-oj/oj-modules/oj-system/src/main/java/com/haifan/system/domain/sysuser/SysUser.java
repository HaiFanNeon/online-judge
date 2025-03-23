package com.haifan.system.domain.sysuser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.haifan.common.core.domin.BaseEntity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author haifan
 * @since 2025-03-16
 */
@Data
@Accessors(chain = true)
@TableName("tb_sys_user")
public class SysUser extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

    private String userAccount;

    private String nickName;

    private String password;


}
