package com.moxuan.sso.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.moxuan.pojo.Users;
import com.moxuan.service.UsersService;
import com.moxuan.utils.*;
import com.moxuan.vo.UsersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.moxuan.constant.CookieKeyConstant.COOKIE_DOMAIN;
import static com.moxuan.constant.CookieKeyConstant.COOKIE_USER_TICKET;
import static com.moxuan.constant.RedisKeyConstant.*;

@Service
public class SsoService {

    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private UsersService usersService;

    public String login(String returnUrl, Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("returnUrl", returnUrl);
        // 如何当前用户登陆了 将请求返回的用户信息/获取说授权信息返回给对应的域名
        String cookieUserTicket = CookieUtils.getCookieValue(request, COOKIE_USER_TICKET.getName());
        if (StrUtil.isNotBlank(cookieUserTicket)) {
            // 校验当前 用户全局授权凭证是否有效
            if (verifyUserTicket(cookieUserTicket)) {
                // 创建用户的临时票据，用于回跳回传 时效 600秒  ->  tmpTicket
                String tmpTicket = generateTmpTicket();
                return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
            }
        }
        // 如果当前用户没有用户信息 说明是第一次登陆 需要在cas认证系统进行认证所以返回登陆的页面
        return "login";
    }

    /**
     * 校验 cookie 中当前用户唯一授权凭证是否有效 cookieUserTicket
     */
    public boolean verifyUserTicket(String cookieUserTicket) {
        String userId = redisOperator.get(REDIS_USER_TICKET.getName() + ":" + cookieUserTicket);
        if (StrUtil.isBlankIfStr(userId)) {
            return false;
        }
        String userVo = redisOperator.get(REDIS_USER_TOKEN.getName() + ":" + userId);
        if (StrUtil.isBlankIfStr(userVo)) {
            return false;
        }
        return true;
    }

    /**
     * 用户未登陆的情况，在cas统一登陆接口
     * 1. 登录后创建用户的全局会话                 ->  uniqueToken
     * 2. 创建用户全局门票，用以表示在CAS端是否登录  ->  userTicket
     * 3. 创建用户的临时票据，用于回跳回传          ->  tmpTicket
     */

    public String doLogin(String username, String password, String returnUrl, Model model, HttpServletRequest request, HttpServletResponse response) {
        // 0. 判断用户名和密码必须不为空
        if (StrUtil.isBlankIfStr(username) ||
                StrUtil.isBlankIfStr(password)) {
            model.addAttribute("errmsg", "用户名或密码不能为空");
            return "login";
        }
        // 查询数据库 是否存在
        Users users = usersService.lambdaQuery()
                .eq(Users::getUsername, username)
                .eq(Users::getPassword, MD5Utils.getMD5Str(password))
                .select(Users::getUsername, Users::getId, Users::getFace, Users::getSex)
                .one();
        if (ObjectUtil.isNull(users)) {
            model.addAttribute("errmsg", "用户名或密码不正确");
            return "login";
        }
        // 用户存在的情况
        // 1. 登录后创建用户的全局会话                 ->  uniqueToken
        this.usersService.generateUserInfo(request, response, users);
        // 2. 创建用户全局门票，用以表示在CAS端是否登录  ->  userTicket
        String userTicket = UUID.randomUUID().toString().trim();
        //  配置  CookieUserTicket cas 统一认证端 cookie的唯一票据
        CookieUtils.setCookie(COOKIE_DOMAIN.getName(), response, COOKIE_USER_TICKET.getName(), userTicket, true);
        // CookieUtils.setCookie(request, response, COOKIE_USER_TICKET.getName(), userTicket, true);
        // 将当前用户的唯一凭证存储在redis  key: REDIS_USER_TICKET:userTicket  value: userId
        redisOperator.set(REDIS_USER_TICKET.getName() + ":" + userTicket, users.getId());
        // 3. 创建用户的临时票据，用于回跳回传 时效 600秒          ->  tmpTicket
        String tmpTicket = generateTmpTicket();


        return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
    }

    /**
     * 校验用户的临时票据是否有效 以达到用户是否登陆
     */
    public BaseResp verifyTmpTicket(String tmpTicket, HttpServletRequest request, HttpServletResponse response) {
        // 判断是否为空
        if (StrUtil.isBlankIfStr(tmpTicket)) {
            return ResultUtil.error("用户票据异常1");
        }
        // 判断是否存在redis 中 临时票据 REDIS_USER_TMP_TICKET:tmpTicket
        String redisUserTicketValue = redisOperator.get(REDIS_USER_TMP_TICKET.getName() + ":" + tmpTicket);
        if (StrUtil.isBlankIfStr(redisUserTicketValue)) {
            return ResultUtil.error("用户票据异常2");
        }
        // 校验临时票据 是否有效
        if (!redisUserTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))) {
            return ResultUtil.error("用户票据异常3");
        }
        // 销毁临时票据
        redisOperator.del(REDIS_USER_TMP_TICKET.getName() + ":" + tmpTicket);
        // 从 cookie 中获取 COOKIE_USER_TICKET 票据
        String cookieUserTicket = CookieUtils.getCookieValue(request, COOKIE_USER_TICKET.getName());
        if (StrUtil.isBlankIfStr(cookieUserTicket)) {
            return ResultUtil.error("用户票据异常4");
        }
        // 通过cookie 中获取到的票据 拼接 查询redis 中是存在用户的ID
        String userId = redisOperator.get(REDIS_USER_TICKET.getName() + ":" + cookieUserTicket);
        if (StrUtil.isBlankIfStr(userId)) {
            return ResultUtil.error("用户票据异常5");
        }
        // 获取redis 中 用户的会话
        String usersVo = redisOperator.get(REDIS_USER_TOKEN.getName() + ":" + userId);
        if (StrUtil.isBlankIfStr(usersVo)) {
            return ResultUtil.error("用户票据异常6");
        }
        // 返回正确的用户会话信息
        return ResultUtil.ok(JSONUtil.toBean(usersVo, UsersVO.class));
    }

    /**
     * 登出
     */
    public BaseResp logout(String userId, HttpServletRequest request, HttpServletResponse response) {
        String cookieUserTicket = CookieUtils.getCookieValue(request, COOKIE_USER_TICKET.getName());
        if (!StrUtil.isBlankIfStr(cookieUserTicket)) {
            // 1。删除用户的凭证
            redisOperator.del(REDIS_USER_TICKET.getName() + ":" + cookieUserTicket);
            // 2。删除用户的token
            redisOperator.del(REDIS_USER_TOKEN.getName() + ":" + userId);
            // 3。删除用户的cookie 授权凭证
            CookieUtils.setCookie(COOKIE_DOMAIN.getName(), response, COOKIE_USER_TICKET.getName(), null, true);
            // CookieUtils.setCookie(request, response, COOKIE_USER_TICKET.getName(), null, true);
        }
        //mtv 系统注销登录，music 前端还显示登录状态，这是因为：每个系统前端都基于自己的 cookie，当一个系统退出登录时，只会清空 SSO 的登录状态还有自己系统的 cookie，别的域名系统并不会涉及到。此时就要基于后端拦截了。
        //一般后端会写个拦截器，根据前端传递的 token 去分布式会话中获取当前操作的用户信息，获取不到则表示登录已失效，直接响应失败。

        return ResultUtil.ok();
    }

    /**
     * 生成临时票证 时效为 600秒  存储在 redis 中
     */
    private String generateTmpTicket() {
        String tmpTicket = UUID.randomUUID().toString().trim();
        // 存储在redis 中
        redisOperator.set(REDIS_USER_TMP_TICKET.getName() + ":" + tmpTicket, MD5Utils.getMD5Str(tmpTicket), 600);
        return tmpTicket;
    }


}
