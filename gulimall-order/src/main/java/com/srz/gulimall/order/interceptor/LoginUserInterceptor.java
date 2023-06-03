package com.srz.gulimall.order.interceptor;

import com.srz.common.constant.AuthServerConstant;
import com.srz.common.vo.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author srz
 * @create 2023/5/21 23:27
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MemberRespVo attribute = (MemberRespVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute!=null){
            loginUser.set(attribute);
            return true;
        } else {
            //没登录去登录
            request.getSession().setAttribute("msg","请先进行登录！");
            response.sendRedirect("http://auth.gulimall.com/login.html");
            return false;
        }

    }
}
