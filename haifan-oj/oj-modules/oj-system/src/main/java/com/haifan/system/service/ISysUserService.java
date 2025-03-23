package com.haifan.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haifan.common.core.domin.R;
import com.haifan.common.core.domin.vo.LoginUserVO;
import com.haifan.system.domain.sysuser.SysUser;
import com.haifan.system.domain.sysuser.dto.SysUserSaveDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author haifan
 * @since 2025-03-16
 */
public interface ISysUserService extends IService<SysUser> {


    R<String> login(String username, String password);

    Integer add(SysUserSaveDTO sysUserSaveDTO);

    LoginUserVO info(String token);

    void logout(String token);
}
