package com.monitor.humiture.service;

import com.monitor.humiture.entity.User;
import org.apache.commons.lang3.StringUtils;

public interface UserService {

    void save(User user);

    void UpdatePhoneByOpenId(String phone,String openId);

    User getUserByOpenId(String openId);

    User getUserById(Integer id);
}
