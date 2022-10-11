package com.example.wechat_auth_demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wechat_auth_demo.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class WXController {
    @Value("${oauth:wx:appid}")
    private String appid;

    @Value("${oauth:wx:appsecret}")
    private String appsecret;

    @Value("${oauth:callback:http}")
    private String http;

    @GetMapping("/index")
    public String index() {
        return "/index";
    }

    @GetMapping("/wxlogin")
    public String wxlogin() {
       String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+
               "&redirect_uri="+http+"&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
       return  "redirect:" + url;
    }
    @GetMapping("/wxcallback")
    public String wxcallback(String code) throws IOException {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+
                "&secret="+ appsecret+
                "&code="+code+"&grant_type=authorization_code";
        JSONObject jsonObject = HttpClientUtils.doGet(url);
        String openid =  jsonObject.getString("openid");
        String access_token = jsonObject.getString("access_token");

        System.out.println(jsonObject);
        // 第四步拉取用户信息
        url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token+
                "&openid="+openid+
                "&lang=zh_CN";
        JSONObject userInfoJson = HttpClientUtils.doGet(url);
        System.out.println("UserInfo:" + userInfoJson);
        return "SUCCESS";
    }
}
