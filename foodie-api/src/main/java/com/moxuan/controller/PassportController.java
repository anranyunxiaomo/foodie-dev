package com.moxuan.controller;

import com.moxuan.bo.UserAddBO;
import com.moxuan.bo.UserBO;
import com.moxuan.pojo.Users;
import com.moxuan.service.UsersService;
import com.moxuan.utils.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UsersService userService;

    /**
     * 校验用户名称是否存在
     */
    @GetMapping("/usernameIsExist")
    public BaseResp usernameIsExist(@RequestParam String username) {
        return userService.usernameIsExist(username);
    }

    /**
     * 注册
     */
    @PostMapping("/regist")
    public BaseResp<Users> regist(@RequestBody @Valid UserAddBO userAddBO, HttpServletRequest request,
                                  HttpServletResponse response) {
        return userService.regist(userAddBO, request, response);
    }


    /**
     * 登陆
     */
    @PostMapping("/login")
    public BaseResp<Users> login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        return userService.login(userBO, request, response);
    }

    /**
     * 退出
     */
    @PostMapping("/logout")
    public BaseResp logout(@RequestParam String userId,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        return userService.logout(userId, request, response);
    }


}
