package com.moxuan.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.RedisOperator;
import com.moxuan.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static com.moxuan.constant.CookieKeyConstant.HEADER_USER_ID;
import static com.moxuan.constant.CookieKeyConstant.HEADER_USER_TOKEN;
import static com.moxuan.constant.RedisKeyConstant.REDIS_USER_TOKEN;

@Configuration

public class UserTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisOperator redisOperator;

    /**
     * 请求访问controller之前 拦截用户是否登录 是否携带用户的id 和用户的token
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader(HEADER_USER_ID.getName());
        String userToken = request.getHeader(HEADER_USER_TOKEN.getName());
        if (StrUtil.isBlankIfStr(userId) && StrUtil.isBlankIfStr(userToken)){
            returnErrorInfo(response, ResultUtil.error("登录..."));
            return false;
        }
        String redisUserToken = redisOperator.get(REDIS_USER_TOKEN.getName() + ":" + userId);
        if (StrUtil.isNotBlank(redisUserToken)) {
            returnErrorInfo(response, ResultUtil.error("登录..."));
            return false;
        }else {
            if (!redisUserToken.equals(userToken)) {
                returnErrorInfo(response, ResultUtil.error("账号在异地登录..."));
                return false;
            }
        }
        return true;
    }

    /**
     * 由于 preHandle 作为拦截用户是否登录 需要进行 boolean 无法对于前端进行一个准确反馈 所以需要借助 response
     *
     * @param response
     * @param result
     */
    public void returnErrorInfo(HttpServletResponse response, BaseResp result) {
        OutputStream out = null;
        try {
            response.setCharacterEncoding(CharsetUtil.UTF_8);
            response.setContentType(ContentType.JSON.getValue());
            out = response.getOutputStream();
            out.write(JSONUtil.toJsonPrettyStr(result).getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 请求访问controller之后，渲染视图之前
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 请求访问controller之后，渲染视图之后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
