package com.moxuan.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@RestController
public class SpringSessionController {

    @PostMapping("/setSession")
    public Object setSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new user");
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");
        return "ok";
    }

}
