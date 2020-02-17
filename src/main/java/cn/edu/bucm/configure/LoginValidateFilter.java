package cn.edu.bucm.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


/**
 * @author wangwei
 * @version v1.0.0
 * @description 登录验证过滤器Filter
 * @date 2019-01-10
 */
//@Component
//@WebFilter(filterName="LoginValidateFilter", urlPatterns="/*")
public class LoginValidateFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }

}

