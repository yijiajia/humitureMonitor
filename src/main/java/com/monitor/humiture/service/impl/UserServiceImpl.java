package com.monitor.humiture.service.impl;

import com.monitor.humiture.dao.UserMapper;
import com.monitor.humiture.entity.User;
import com.monitor.humiture.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void save(User user) {

        if(user!=null && user.getOpenId()!=null){
            userMapper.insert(user);
        }

    }

    @Override
    public void UpdatePhoneByOpenId(String phone,String openId) {

        if(StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(openId)){
            userMapper.updatePhone(phone,openId);
        }

    }

    @Override
    public User getUserByOpenId(String openId) {

        if(StringUtils.isNotEmpty(openId)){
            return userMapper.selectByOpenId(openId);
        }
        return null;
    }

    @Override
    public User getUserById(Integer id) {

        if(id!=null){
            return userMapper.selectById(id);
        }
        return null;
    }
}
