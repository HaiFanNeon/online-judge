package com.haifan.system.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.haifan.common.core.domin.LoginUser;
import com.haifan.common.core.domin.R;
import com.haifan.common.core.domin.vo.LoginUserVO;
import com.haifan.common.core.enums.ResultCode;
import com.haifan.common.core.enums.UserIdentity;

import com.haifan.common.security.exceptionHandler.ServiceException;
import com.haifan.common.security.service.TokenService;

import com.haifan.system.domain.SysUser;
import com.haifan.system.domain.dto.SysUserSaveDTO;
import com.haifan.system.mapper.SysUserMapper;
import com.haifan.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haifan.system.utils.BCryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author haifan
 * @since 2025-03-16
 */
@RefreshScope
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;


    @Override
    public R<String> login(String userAccount, String password) {
        // 通过账号在数据库中查询对应管理员的信息
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysUser::getUserId, SysUser::getNickName,SysUser::getPassword).eq(SysUser::getUserAccount, userAccount);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        // 判断用户是否存在，密码是否正确，以及用户名或者密码是否错误
        if (sysUser == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        } else if (BCryptUtils.matchesPassword(password, sysUser.getPassword())) {
            String token = tokenService.createToken(sysUser.getUserId(), secret, UserIdentity.ADMIN.getValue(), sysUser.getNickName());
            return R.ok(token);
        }
        return R.fail(ResultCode.FAILED_LOGIN);
    }

    @Override
    public Integer add(SysUserSaveDTO sysUserSaveDTO) {
        List<SysUser> sysUserList = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserAccount, sysUserSaveDTO.getUserAccount()));
        if (!CollectionUtil.isEmpty(sysUserList)) {
            // 用户已经存在
            throw new ServiceException(ResultCode.AILED_USER_EXISTS.getMsg());
        }
        // 将dto转化为实体类
        SysUser sysUser = BeanUtil.copyProperties(sysUserSaveDTO, SysUser.class);
        sysUser.setPassword(BCryptUtils.encryptPassword(sysUser.getPassword()));
//        sysUser.setCreateBy(100L); // 获取当前用户用户id， 如何获取当前调用接口的用户id呢？
        int i = sysUserMapper.insert(sysUser);
        return i;
    }

    @Override
    public LoginUserVO info(String token) {

        LoginUser loginUser = tokenService.getLoginUser(token, secret);
        if (loginUser == null) {
            log.info("loginUser 为空");
            return null;
        }

        LoginUserVO loginUserVO = BeanUtil.copyProperties(loginUser, LoginUserVO.class);
        log.info(loginUserVO.toString());
        return loginUserVO;
    }
}
