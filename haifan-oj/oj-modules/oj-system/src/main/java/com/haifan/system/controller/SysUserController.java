package com.haifan.system.controller;


import com.haifan.common.core.controller.BaseController;
import com.haifan.common.core.domin.R;
import com.haifan.system.domain.dto.LoginDTO;
import com.haifan.system.domain.dto.SysUserSaveDTO;
import com.haifan.system.domain.vo.SysUserVO;
import com.haifan.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author haifan
 * @since 2025-03-16
 */
@Tag(name = "管理员接口")
@Slf4j
@RestController
@RequestMapping("/sysuser")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    @PostMapping("/login")
    @Operation(summary = "管理员登录", description = "根据账号密码进行管理员登录")
    @ApiResponse(responseCode = "1000", description = "操作成功")
    @ApiResponse(responseCode = "2000", description = "服务繁忙请稍后重试")
    @ApiResponse(responseCode = "3102", description = "用户不存在")
    @ApiResponse(responseCode = "3103", description = "用户名或密码错误")
    public R<String> login(@Validated @RequestBody LoginDTO loginDTO) {
        return sysUserService.login(loginDTO.getUserAccount(), loginDTO.getPassword());
    }

    @Operation(summary = "新增管理员", description = "根据提供的信息新增管理员")
    @ApiResponse(responseCode = "1000", description = "操作成功")
    @ApiResponse(responseCode = "2000", description = "服务繁忙请稍后重试")
    @ApiResponse(responseCode = "3101", description = "用户已存在")
    @PostMapping("/add")
    public R add(@Validated @RequestBody SysUserSaveDTO sysUserSaveDTO) {
        return toResult(sysUserService.add(sysUserSaveDTO));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户")
    @Parameters(value = {
            @Parameter(name = "userId", in = ParameterIn.PATH, description = "用户ID")
    })
    @ApiResponse(responseCode = "1000", description = "成功删除用户")
    @ApiResponse(responseCode = "2000", description = "服务繁忙请稍后重试")
    @ApiResponse(responseCode = "3102", description = "用户不存在")
    public R delete(@PathVariable Long userId) {
        return null;
    }


    @GetMapping("/detail")
    @Operation(summary = "用户详细", description = "根据查询条件查询用户详情")
    @Parameters(value = {
            @Parameter(name = "userId", in = ParameterIn.QUERY, description = "用户ID"),
            @Parameter(name = "sex", in = ParameterIn.QUERY, description = "用户性别")
    })
    @ApiResponse(responseCode = "1000", description = "成功获取用户信息")
    @ApiResponse(responseCode = "2000", description = "服务繁忙请稍后重试")
    @ApiResponse(responseCode = "3102", description = "用户不存在")
    public R<SysUserVO> detail(@RequestParam(required = true) Long userId, @RequestParam(required = false) String sex) {
        return null;
    }
}
