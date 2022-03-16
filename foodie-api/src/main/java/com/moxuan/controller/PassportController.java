package com.moxuan.controller;

import com.moxuan.service.UsersService;
import com.moxuan.utils.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UsersService userService;

    @PostMapping("/usernameIsExist")
    public BaseResp usernameIsExist(@RequestParam String username){
        return userService.usernameIsExist(username);
    }


}
