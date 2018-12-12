package com.monitor.humiture.controller;

import com.monitor.humiture.HumitureApplication;
import com.monitor.humiture.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HumitureApplication.class)
public class UserControllerTest {

    @Test
    public void testEquals() {

        User user = new User();
        user.setGender("0");
        System.out.println("0".equals(user.getGender())?"男":"女");

    }
}