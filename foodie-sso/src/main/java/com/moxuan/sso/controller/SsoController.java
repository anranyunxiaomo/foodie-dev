package com.moxuan.sso.controller;

import io.swagger.models.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class SsoController {


    @GetMapping("/login")
    public String login(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response){
        return null;
    }

}
