package com.moxuan.controller;

import com.moxuan.bo.UserAddBO;
import com.moxuan.service.UsersService;
import com.moxuan.utils.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public BaseResp regist(@RequestBody @Valid UserAddBO userAddBO) {
        return userService.regist(userAddBO);
    }


}
