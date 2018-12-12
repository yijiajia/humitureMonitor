package com.monitor.humiture.dao;

import com.monitor.humiture.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper{

    @Insert("insert into user(open_id,nick_name,gender,city,province,country,avatar_url,phone)" +
            " values(#{openId},#{nickName},#{gender},#{city},#{province},#{country},#{avatarUrl},#{phone})")
    int insert(User user);

    @Delete("delete from user where open_id=#{openId}")
    void deleteByPrimaryKey(String openId);

    @Results(id = "BaseResult",
    value ={
            @Result(property = "id",column = "id"),
            @Result(property = "openId",column = "open_id"),
            @Result(property = "nickName",column = "nick_name"),
            @Result(property = "gender",column = "gender"),
            @Result(property = "city",column = "city"),
            @Result(property = "province",column = "province"),
            @Result(property = "country",column = "country"),
            @Result(property = "avatarUrl",column = "avatar_url"),
            @Result(property = "phone",column = "phone")
    })
    @Select("select * from user where open_id=#{openId}")
    User selectByOpenId(String openId);

    @ResultMap("BaseResult")
    @Select("select * from user where id=#{id}")
    User selectById(Integer id);

    @Update("update user set nick_name=#{nickName} where open_id=#{openId}")
    void updateByOpenId(User record);

    @ResultMap("BaseResult")
    @Select("select * from user")
    List<User> findAllUser();

    @Update("update user  set phone=#{phone} where open_id=#{openId}")
    void updatePhone(@Param(value = "phone") String phone,@Param(value = "openId") String openId);



}
