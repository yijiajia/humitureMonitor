package com.monitor.humiture.service.impl;

import com.monitor.humiture.dao.UserDeviceMapper;
import com.monitor.humiture.entity.UserDevice;
import com.monitor.humiture.service.UserDeviceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    @Override
    public List<UserDevice> getUserDevice(Integer uId) {

        if(uId!=null){
            List<UserDevice> list = userDeviceMapper.selectDeviceByUid(uId);
            return list;
        }
        return null;
    }

    @Override
    public void updateDeviceName(String name, String deviceId) {

        if(!StringUtils.isAnyBlank(name,deviceId)){
            userDeviceMapper.updateNameByDeviceId(name,deviceId);
        }
    }

    @Override
    public void add(UserDevice userDevice) {

        if(userDevice!=null && userDevice.getDeviceId()!=null && userDevice.getUid()!=null){
            userDeviceMapper.insert(userDevice);
        }

    }

    @Override
    public UserDevice getDevice(String deviceId) {

        if(StringUtils.isNotEmpty(deviceId)){
            return userDeviceMapper.selectByDeviceId(deviceId);
        }
        return null;
    }
}
