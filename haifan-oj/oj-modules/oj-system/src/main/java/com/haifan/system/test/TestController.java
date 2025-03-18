package com.haifan.system.test;

import com.haifan.common.redis.service.RedisService;
import com.haifan.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/redisAddAndGet")
    public String redisAddAndGet() {
        redisService.setCacheObject("user", new SysUser().setUserId(1L).setUserAccount("123"));
        SysUser user = redisService.getCacheObject("user", SysUser.class);
        System.out.println(user);
        return "ok";
    }

}
