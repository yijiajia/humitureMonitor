package com.monitor.humiture.dao;

import com.monitor.humiture.HumitureApplication;
import com.monitor.humiture.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HumitureApplication.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert(){
        User user = new User();
        user.setOpenId("1001");
        user.setNickName("李老四");
        user.setCity("广州");
        user.setGender("男");
        user.setProvince("广东");
        user.setCountry("china");
        user.setPhone("1");
        user.setAvatarUrl("01.jpg");

        int i = userMapper.insert(user);
        Assert.assertEquals(1,i);
    }

    @Test
    public void selectAllUser(){

        List<User> userList = userMapper.findAllUser();
        Assert.assertNotNull(userList);
        System.out.println(userList);
    }

    @Test
    public void testUserMapper(){

        //插入测试
        User user = new User();
        user.setOpenId("12314");
        user.setNickName("测试用户");
        user.setCity("广州");
        user.setGender("男");
        user.setProvince("广东");
        user.setCountry("china");
        user.setPhone("1353914991");
        user.setAvatarUrl("01.jpg");

        //更新测试
        user.setNickName("张三");
        userMapper.updateByOpenId(user);
//        该注释取消后会报错
//        user = userMapper.selectByPrimaryKey("12314");
//        Assert.assertEquals("张三",user.getNickName());

        //删除测试
        userMapper.deleteByPrimaryKey("12314");
        user = userMapper.selectByOpenId("12314");
        Assert.assertEquals(null,user);


    }

    @Test
    public void testUpdatePhone(){
        userMapper.updatePhone("13539144998","1001");
    }


    @Test
    public void testSelectById(){
       User user =  userMapper.selectById(6);
       Assert.assertNotNull(user);
    }




}