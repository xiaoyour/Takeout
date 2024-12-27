package com.sky.service;


import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.vo.UserLoginVO;
import org.springframework.stereotype.Service;


/**
 * 用户服务层
 */

public interface UserService {

    public User login(UserLoginDTO userLoginDTO);

}
