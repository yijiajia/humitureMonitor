package com.monitor.humiture.service.impl;

import com.monitor.humiture.HumitureApplication;
import com.monitor.humiture.entity.User;
import com.monitor.humiture.entity.UserDevice;
import com.monitor.humiture.service.UserDeviceService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HumitureApplication.class)
public class UserDeviceServiceImplTest {

    @Autowired
    private UserDeviceService userDeviceService;

    @Test
    public void getUserDevice() {
    }

    @Test
    public void updateDeviceName() {
    }

    @Test
    public void getDevice() {

        UserDevice userDevice =  userDeviceService.getDevice("x1");
        Assert.assertEquals("设备",userDevice.getDeviceName());
    }
}