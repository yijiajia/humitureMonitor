package com.monitor.humiture.service;

import com.monitor.humiture.entity.UserDevice;

import java.util.List;

public interface UserDeviceService {

    List<UserDevice> getUserDevice(Integer uid);

    void updateDeviceName(String name,String deviceId);

    void add(UserDevice userDevice);

    UserDevice getDevice(String deviceId);
}
