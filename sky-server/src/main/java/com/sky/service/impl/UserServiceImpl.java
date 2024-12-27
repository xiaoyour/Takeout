package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
@Service
public class UserServiceImpl implements UserService {


   @Autowired
    WeChatProperties weChatProperties;
   @Autowired
   UserMapper userMapper;

    private static final String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 微信用户登录获取用户信息
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
//        通过微信授权码code获取openid
        String openid = getOpenid(userLoginDTO.getCode());

//        判断openid是否为空，判断是否登录成功
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
//        查询用户是否为新用户
         User user = userMapper.getByOpenid(openid);
        //        是新用户则放入User表中
        if (user == null) {
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insert(user);
        }

        return user;
    }

    /**
     * 根据code值获取openid
     * @param code
     * @return
     */
    private String getOpenid(String code) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("appid",weChatProperties.getAppid());
        hashMap.put("secret",weChatProperties.getSecret());
        hashMap.put("js_code",code);
        hashMap.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(LOGIN_URL, hashMap);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");

        return openid;
    }
}
