package com.moxuan.sso.controller;

import com.moxuan.sso.service.SsoService;
import com.moxuan.utils.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class SsoController {


    @Autowired
    private SsoService ssoService;

    @GetMapping("/login")
    public String login(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        return ssoService.login(returnUrl, model, request, response);
    }

    /**
     * 用户未登陆的情况，在cas统一登陆接口
     * 1. 登录后创建用户的全局会话                 ->  uniqueToken
     * 2. 创建用户全局门票，用以表示在CAS端是否登录  ->  userTicket
     * 3. 创建用户的临时票据，用于回跳回传          ->  tmpTicket
     */
    @PostMapping("/doLogin")
    public String doLogin(String username,
                          String password,
                          String returnUrl,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        return ssoService.doLogin(username, password, returnUrl, model, request, response);
    }

    /**
     * 校验用户的临时票据是否有效 以达到用户是否登陆
     */
    @PostMapping("/verifyTmpTicket")
    @ResponseBody
    public BaseResp verifyTmpTicket(String tmpTicket,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        return ssoService.verifyTmpTicket(tmpTicket, request, response);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    @ResponseBody
    public BaseResp logout(String userId,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        return ssoService.logout(userId, request, response);
    }
}
