package com.moxuan.controller.center;

import com.moxuan.service.center.CenterUserService;
import com.moxuan.utils.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("center")
public class CenterController {


    @Autowired
    private CenterUserService centerUserService;


    /**
     * 用户中心-用户信息
     */
    @GetMapping("/userInfo")
    public BaseResp userInfo(@RequestParam String userId) {
        return centerUserService.userInfo(userId);
    }

}
