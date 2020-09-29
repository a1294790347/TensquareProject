package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.BaseClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//连接到base服务器 fallback:如果连接客户端出问题自动走fallback指定的类
@FeignClient(value = "tensquare-base", fallback = BaseClientImpl.class)
public interface BaseClient {
    //调用base服务器中的findById需要将全路径写上
    @RequestMapping(value = "/label/{id}",method = RequestMethod.GET)
    //@PathVariable("labelId")括号内必须得写占位符名称，不管后面
    public Result findById(@PathVariable("id") String id);
}
