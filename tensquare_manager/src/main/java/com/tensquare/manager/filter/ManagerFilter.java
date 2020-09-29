package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 过滤器类型
     * */
    @Override
    public String filterType() {
        /**
         * return的值
         * pre: 在执行之前进行过滤
         * route: 在路由请求时候被调用
         * post: 当前过滤器在操作之后进行过滤
         * error: 处理请求时发生错误被调用
         * */
        return "pre";
    }

    /**
    * 多个过滤器的执行顺序，数字越小，表示越先执行
    * */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 当前过滤器是否开启true表示开启
     * */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的操作，return任何object的值都表示继续执行
     * setsendzullRespponse(false)表示不再继续执行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("经过后台过滤器了");
        RequestContext requestContext = RequestContext.getCurrentContext();
        //request域
        HttpServletRequest request = requestContext.getRequest();

        //转发到对应的客户端时，不会包含头信息，此时需要对其做单独处理，以至程序正常执行
        if (request.getMethod().equals("OPTIONS")){
            return null;
        }

        //登录时不需要admin权限，登录放行
        if (request.getRequestURL().indexOf("login")>0){
            return null;
        }

        //得到头信息
        String header = request.getHeader("Authorization");
        if (header != null && !"".equals(header)){
            if (header.startsWith("Bearer ")){
                String token = header.substring(8);
                try{
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if (roles.equals("admin")){
                        //把头信息转发下去，并且继续放行
                        requestContext.addZuulRequestHeader("Authorization", header);
                        return null;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    requestContext.setSendZuulResponse(false);//终止运行
                }
            }
        }
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(403);
        requestContext.setResponseBody("权限不足");
        requestContext.getResponse().setContentType("text/html:charset=utf-8");
        return null;
    }
}
