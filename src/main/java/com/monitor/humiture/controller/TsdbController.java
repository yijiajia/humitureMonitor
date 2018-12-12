package com.monitor.humiture.controller;

import com.baidubce.services.tsdb.model.Result;
import com.monitor.humiture.annotation.Login;
import com.monitor.humiture.annotation.LoginUser;
import com.monitor.humiture.constant.TsdbConstant;
import com.monitor.humiture.entity.User;
import com.monitor.humiture.entity.UserDevice;
import com.monitor.humiture.handler.TsdbHandler;
import com.monitor.humiture.service.TsdbService;
import com.monitor.humiture.service.UserDeviceService;
import com.monitor.humiture.vo.HumitureVO;
import com.monitor.humiture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/tsdb")
public class TsdbController {

    @Autowired
    private TsdbService tsdbService;

    @Autowired
    private UserDeviceService userDeviceService;

    @ResponseBody
    @RequestMapping(value = "/getChartData",method = RequestMethod.POST)
    public HumitureVO getChartData(String deviceId){

        String field = TsdbConstant.Filed;
        HumitureVO humitureVO = new HumitureVO();

        //计算08-09 18:00:00 到 08-10 18:00:00时间段的数据值
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateEnd = sdf.parse("2018-08-10 18:00:00");
            long end = dateEnd.getTime();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateEnd);
            calendar.add(Calendar.HOUR_OF_DAY,-24);
            Date dateStart = calendar.getTime();
            long start = dateStart.getTime();

            //10min 统计一次
            List<Result> tempList = tsdbService.queryData(TsdbConstant.METRIC_TEMP,field,start,end,TsdbConstant.TAG_NAME,deviceId,TsdbConstant.SAMPLING_10MINUTE);
            List<Result> humpList = tsdbService.queryData(TsdbConstant.METRIC_HUM,field,start,end,TsdbConstant.TAG_NAME,deviceId,TsdbConstant.SAMPLING_10MINUTE);

            List<String> timeList = new ArrayList<>();
            List<Double> temperature = new ArrayList<>();
            List<Double> humidity = new ArrayList<>();

            //处理温湿度数据，格式化时间戳为指定格式（MM/dd mm:ss)
            TsdbHandler.handleData(tempList, timeList, temperature);
            TsdbHandler.handleData(humpList, timeList, humidity);

            humitureVO.setHumidity(humidity)
                    .setTemperature(temperature)
                    .setTime(timeList);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return humitureVO;
    }


    @Login
    @RequestMapping(value = "/bind",method = RequestMethod.POST)
    public ResultVO<String> bindDevice(@LoginUser User user, String deviceId){

        log.info("用户绑定设备，设备id:{}",deviceId);
//        Integer userId = (Integer) request.getAttribute(AuthorizationInterceptor.USER_KEY);

        //若设备被绑定，则返回
        if(userDeviceService.getDevice(deviceId)!=null){
            return ResultVO.ResultBuilder.getError(505,"设备已被绑定");
        }
        //TODO 判断设备是否存在,利用tsdb数据库.同时userDevice增加metric
        Map<String,List<String>> mapList = tsdbService.getTagValueForDevice();
        boolean flag = false;
        for(List<String> list:mapList.values()){
            if(list.contains(deviceId)){
                flag = true;  //说明存在该设备编号
                break;
            }
        }
        if(!flag){
            return ResultVO.ResultBuilder.getError(504,"设备编号不存在");
        }

        UserDevice userDevice = new UserDevice();

        userDevice.setUid(user.getId());
        userDevice.setDeviceId(deviceId);
        userDevice.setDeviceName("设备");

        userDeviceService.add(userDevice);

        return ResultVO.ResultBuilder.getOK();

    }

    @Login
    @RequestMapping(value = "/getDeviceList")
    public ResultVO<String> getDeviceList(@LoginUser User user){

        log.info("用户：{}获取设备信息",user.getNickName());
        List<UserDevice> list = userDeviceService.getUserDevice(user.getId());

        //TODO 查询设备当前温湿度,需增加tsdbService.getDataforDevice(metric,deviceId);
        for(UserDevice userDevice:list){
            String deviceId = userDevice.getDeviceId();
        }



        if(list!=null){
            return ResultVO.ResultBuilder.getOK(list);
        }else{
            return ResultVO.ResultBuilder.getOK(null);
        }


    }




}
