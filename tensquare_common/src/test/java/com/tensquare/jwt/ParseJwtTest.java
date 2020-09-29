package com.tensquare.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

public class ParseJwtTest {
    public static void main(String[] args) {
        Claims claims = Jwts.parser().setSigningKey("itcast")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLpmpTlo4HogIHnjosiLCJpYXQiOjE2MDAxNzIzMzUsImV4cCI6MTYwMDE3MjkzNSwicm9sZSI6ImFkbWluIn0.wtVEw8aFfa0Q9ne_qApBu7LCG31PU3SVyaceIj2Eqzo")
                .getBody();
        System.out.println("用户id:"+claims.getId());
        System.out.println("用户名:"+claims.getSubject());
        //format()返回一个字符串,parse返回一个日期
        System.out.println("登录时间:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
        System.out.println("过期时间:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));
        System.out.println("用户角色:"+ claims.get("role"));
    }
}
