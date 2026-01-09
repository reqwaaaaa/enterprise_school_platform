package com.school_enterprise_platform.controller;

import com.school_enterprise_platform.dto.LoginDTO;
import com.school_enterprise_platform.entity.User;
import com.school_enterprise_platform.service.UserService;
import com.school_enterprise_platform.utils.JwtUtil;
import com.school_enterprise_platform.dto.RegisterDTO;
import com.school_enterprise_platform.vo.LoginVO;
import com.school_enterprise_platform.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO.getUsername());

        User user = userService.login(loginDTO);

        String token = jwtUtil.generateToken(user.getUsername());

        LoginVO vo = LoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(Integer.valueOf(user.getUserRole()))
                .token(token)
                .build();

        return Result.success(vo);
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success("注册成功");
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("登出成功");
    }
}