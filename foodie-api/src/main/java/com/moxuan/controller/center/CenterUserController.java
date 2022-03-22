package com.moxuan.controller.center;

import com.moxuan.pojo.center.CenterUserBO;
import com.moxuan.service.center.CenterUserService;
import com.moxuan.utils.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("userInfo")
public class CenterUserController {

    @Autowired
    private CenterUserService centerUserService;

    /**
     * 修改用户信息
     */
    @PostMapping("update")
    public BaseResp updateUserInfo(@RequestParam String userId,
                                   @RequestBody @Validated CenterUserBO centerUserBO,
                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        return centerUserService.updateUserInfo(userId,
                centerUserBO,
                result,
                request,
                response);
    }

    /**
     * 更新用户的头像
     */
    @PostMapping("/uploadFace")
    public BaseResp uploadFace(@RequestParam String userId,
                               MultipartFile file,
                               HttpServletRequest request, HttpServletResponse response) {
        return centerUserService.uploadFace(userId, file, request, response);
    }

}
