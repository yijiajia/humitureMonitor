package com.monitor.humiture.dao;

import com.monitor.humiture.HumitureApplication;
import com.monitor.humiture.entity.UserDevice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HumitureApplication.class)
public class UserDeviceMapperTest {

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    @Test
    public void insert() {
        UserDevice userDevice = new UserDevice();
        userDevice.setDeviceId("1002");
        userDevice.setDeviceName("温湿度");
        userDevice.setUid(1001);
        userDeviceMapper.insert(userDevice);

    }

    @Test
    public void deleteById() {
        userDeviceMapper.deleteById(1);
    }

    @Test
    public void selectById() {
        UserDevice userDevice = userDeviceMapper.selectById(1);
        System.out.println(userDevice);
        Assert.assertNotNull(userDevice);
    }

    @Test
    public void selectDeviceByOpenId() {
        List<UserDevice> list= userDeviceMapper.selectDeviceByUid(1001);
        System.out.println(list);
        Assert.assertNotNull(list);
    }

    @Test
    public void updateNameByDeviceId() {
        userDeviceMapper.updateNameByDeviceId("温湿度计","1001");
    }
}