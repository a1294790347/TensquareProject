package com.tensquare.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SendSms {
    //对应你阿里云账户的accessKeyId
    private static final String accessKeyId="LTAI4G8qSU2W8MpPmgTC2ZxP";
    //对应你阿里云账户的accessKeySecret
    private static final String accessKeySecret="DEU3BQbLLruopLp8bj6hs12DgcJJ8l";
    //对应你阿里云的签名名称
    private static final String signName="十次方项目练习";
    //对应你阿里云的模板code
    private static final String templateCode="SMS_202730066";

    public static void SendCode(String telphone, String code) {
        //参数服务器名称
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //请求方式
        request.setSysMethod(MethodType.POST);
        //请求地址
        request.setSysDomain("dysmsapi.aliyuncs.com");
        //对应版本号
        request.setSysVersion("2017-05-25");
        //需要什么业务
        request.setSysAction("SendSms");
        //电话号码
        request.putQueryParameter("PhoneNumbers", telphone);
        //签名
        request.putQueryParameter("SignName", signName);
        //模板code
        request.putQueryParameter("TemplateCode", templateCode);
        //验证码
        request.putQueryParameter("TemplateParam", "{\"code\":"+code+"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
