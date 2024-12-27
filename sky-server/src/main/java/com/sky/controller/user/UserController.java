package com.sky.controller.user;


import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@Api(tags = "用户控制")
@Slf4j
@RequestMapping("/user/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    JwtProperties jwtProperties;


    /**
     * "用户登录"
     * @param userLoginDTO
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO) {
//      获取用户登录信息
        User loginUser = userService.login(userLoginDTO);
        log.info("用户登录:{}", loginUser.getOpenid());
//       通过微信用户唯一标识符openid创建JWT令牌
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(JwtClaimsConstant.USER_ID, loginUser.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), hashMap);
//        创建返回对象
        UserLoginVO userLoginVO = UserLoginVO.builder().id(loginUser.getId())
                .openid(loginUser.getOpenid())
                .token(token).build();

        return Result.success(userLoginVO);

    }
}
