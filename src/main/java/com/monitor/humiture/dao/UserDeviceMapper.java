package com.monitor.humiture.dao;

import com.monitor.humiture.entity.User;
import com.monitor.humiture.entity.UserDevice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDeviceMapper {

    @Insert("insert into user_device(device_id,device_name,uid) values(#{deviceId},#{deviceName},#{uid}) ")
    int insert(UserDevice userDevice);

    @Delete("delete from user_device where id = #{id}")
    void deleteById(Integer id);

    @ResultMap("BaseResult")
    @Select("select * from user_device where id=#{id}")
    UserDevice selectById(Integer id);

    @Results(id = "BaseResult",value = {
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "deviceName",column = "device_name"),
            @Result(property = "uid",column = "uid")
    })
    @Select("select * from user_device where uid = #{uid}")
    List<UserDevice> selectDeviceByUid(@Param(value = "uid") Integer uid);


    @Update("update user_device set device_name = #{name} where device_id=#{deviceId}")
    void updateNameByDeviceId(@Param(value = "name") String name,@Param(value = "deviceId") String deviceId);

    @ResultMap("BaseResult")
    @Select("select * from user_device where device_id = #{deviceId}")
    UserDevice selectByDeviceId(String deviceId);
}
