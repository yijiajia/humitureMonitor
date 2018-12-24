package com.monitor.humiture.controller;

import com.baidubce.services.tsdb.model.Result;
import com.monitor.humiture.annotation.Login;
import com.monitor.humiture.annotation.LoginUser;
import com.monitor.humiture.constant.TsdbConstant;
import com.monitor.humiture.dto.HumStatusDTO;
import com.monitor.humiture.entity.User;
import com.monitor.humiture.entity.UserDevice;
import com.monitor.humiture.handler.TsdbHandler;
import com.monitor.humiture.service.TsdbService;
import com.monitor.humiture.service.UserDeviceService;
import com.monitor.humiture.vo.HumitureDeviceVO;
import com.monitor.humiture.vo.HumitureVO;
import com.monitor.humiture.vo.R;
import com.monitor.humiture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/tsdb")
public class TsdbController {

    @Autowired
    private TsdbService tsdbService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private TsdbHandler tsdbHandler;

    /**
     * 获取设备24小时内的数据
     * @param deviceId
     * @return
     */
    @Login
    @RequestMapping(value = "/getChartData",method = RequestMethod.POST)
    public ResultVO<HumitureVO> getChartData(String deviceId){

        //判断deviceId是否存在
        if(userDeviceService.getDevice(deviceId)==null){
            log.info("\n请求参数为{}",deviceId);
            return ResultVO.ResultBuilder.getError("参数有误，请检查");
        }

        String field = TsdbConstant.Filed;
        HumitureVO humitureVO = new HumitureVO();

        try {
            List<Result> tempList = null;
            List<Result> humpList = null;
            //若deviceId为y1则查询具体时间内的数据（demo)
            if(TsdbConstant.TAG_VALUE2.equals(deviceId) || TsdbConstant.TAG_VALUE1.equals(deviceId)){

                //计算08-09 18:00:00 到 08-10 18:00:00时间段的数据值
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateEnd = sdf.parse("2018-08-10 18:00:00");
                long end = dateEnd.getTime();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateEnd);
                calendar.add(Calendar.HOUR_OF_DAY,-24);
                Date dateStart = calendar.getTime();
                long start = dateStart.getTime();

                //10min 统计一次
                tempList = tsdbService.queryData(TsdbConstant.METRIC_TEMP,
                                                field,start,end,
                                                TsdbConstant.TAG_NAME,deviceId,
                                                TsdbConstant.SAMPLING_10MINUTE);
                humpList = tsdbService.queryData(TsdbConstant.METRIC_HUM,
                                                field,start,end,
                                                TsdbConstant.TAG_NAME,deviceId,
                                                TsdbConstant.SAMPLING_10MINUTE);
            }else{
                //查询用户设备24小时内的数据
                tempList = tsdbService.queryData(TsdbConstant.METRIC_TEMP,
                                                 deviceId,
                                                 TsdbConstant.SAMPLING_10MINUTE);
                humpList = tsdbService.queryData(TsdbConstant.METRIC_HUM,
                                                deviceId,
                                                TsdbConstant.SAMPLING_10MINUTE);

            }



            List<String> timeList = new ArrayList<>();
            List<Double> temperature = new ArrayList<>();
            List<Double> humidity = new ArrayList<>();

            //处理温湿度数据，格式化时间戳为指定格式（MM/dd mm:ss)
            tsdbHandler.handleData(tempList, timeList, temperature);
            tsdbHandler.handleData(humpList, timeList, humidity);

            humitureVO.setHumidity(humidity)
                    .setTemperature(temperature)
                    .setTime(timeList);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return ResultVO.ResultBuilder.getOK(humitureVO);
    }

    /**
     * 用户绑定设备
     * @param user
     * @param deviceId
     * @return
     */
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
        userDevice.setDeviceName("设备"+deviceId);

        userDeviceService.add(userDevice);

        return ResultVO.ResultBuilder.getOK();

    }

    /**
     * 获取设备列表
     * @param user
     * @return
     */
    @Login
    @RequestMapping(value = "/getDeviceList")
    public ResultVO<String> getDeviceList(@LoginUser User user){

        log.info("用户：{}获取设备信息",user.getNickName());
        List<UserDevice> list = userDeviceService.getUserDevice(user.getId());
        List<Result> results = null;
        List<HumitureDeviceVO> humitureDeviceVOS = new LinkedList<>();

        //TODO 查询设备当前温湿度,需增加tsdbService.getDataforDevice(metric,deviceId);
        for(UserDevice userDevice:list){

            HumitureDeviceVO humitureDataVO = new HumitureDeviceVO();
            String deviceId = userDevice.getDeviceId();

            results = tsdbService.getDataForDevice(TsdbConstant.METRIC_TEMP,deviceId);
            humitureDataVO.setTemperature(tsdbHandler.handleHumitureData(results));
            //处理温度状态
            HumStatusDTO humStatusDTO = tsdbHandler.handleStatus(results);

            results = tsdbService.getDataForDevice(TsdbConstant.METRIC_HUM,deviceId);
            humitureDataVO.setHumidity(tsdbHandler.handleHumitureData(results));

            humitureDataVO.setName(userDevice.getDeviceName());
            humitureDataVO.setDeviceId(deviceId);


            humitureDataVO.setStatus(humStatusDTO.getStatus());
            humitureDataVO.setImgUrl(humStatusDTO.getImgUrl());
            humitureDeviceVOS.add(humitureDataVO);
        }


        if(humitureDeviceVOS!=null){
            return ResultVO.ResultBuilder.getOK(humitureDeviceVOS);
        }else{
            return ResultVO.ResultBuilder.getOK(null);
        }


    }


    /**
     * 获取单个设备的数据
     * @param deviceId
     * @return
     */
    @Login
    @RequestMapping(value = "/getDeviceData",method = RequestMethod.POST)
    public ResultVO<String> getDeviceData(String deviceId){

        List<Result> results = null;
        HumitureDeviceVO humitureDataVO = new HumitureDeviceVO();
        UserDevice userDevice = userDeviceService.getDevice(deviceId);

        results = tsdbService.getDataForDevice(TsdbConstant.METRIC_TEMP,deviceId);
        humitureDataVO.setTemperature(tsdbHandler.handleHumitureData(results));
        //处理温度状态
        HumStatusDTO humStatusDTO = tsdbHandler.handleStatus(results);

        results = tsdbService.getDataForDevice(TsdbConstant.METRIC_HUM,deviceId);
        humitureDataVO.setHumidity(tsdbHandler.handleHumitureData(results));

        humitureDataVO.setName(userDevice.getDeviceName());
        humitureDataVO.setDeviceId(deviceId);
        humitureDataVO.setStatus(humStatusDTO.getStatus());
        humitureDataVO.setImgUrl(humStatusDTO.getImgUrl());

        return ResultVO.ResultBuilder.getOK(humitureDataVO);
    }

    /**
     * 获取示例数据
     * * @return
     */
    @RequestMapping(value = "/demo/getData")
    public R getDemoData(){

        Map<String,Object> map = new HashMap<>();
        try{
            HumitureVO humitureVO = new HumitureVO();
            List<Result> tempList = null;
            List<Result> humpList = null;
            //计算08-09 18:00:00 到 08-10 18:00:00时间段的数据值
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateEnd = sdf.parse("2018-08-10 18:00:00");
            long end = dateEnd.getTime();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateEnd);
            calendar.add(Calendar.HOUR_OF_DAY,-24);
            Date dateStart = calendar.getTime();
            long start = dateStart.getTime();

            //10min 统计一次
            tempList = tsdbService.queryData(TsdbConstant.METRIC_TEMP,
                    TsdbConstant.Filed,start,end,
                    TsdbConstant.TAG_NAME,TsdbConstant.TAG_VALUE2,
                    TsdbConstant.SAMPLING_10MINUTE);
            humpList = tsdbService.queryData(TsdbConstant.METRIC_HUM,
                    TsdbConstant.Filed,start,end,
                    TsdbConstant.TAG_NAME,TsdbConstant.TAG_VALUE2,
                    TsdbConstant.SAMPLING_10MINUTE);

            List<String> timeList = new ArrayList<>();
            List<Double> temperature = new ArrayList<>();
            List<Double> humidity = new ArrayList<>();

            //处理温湿度数据，格式化时间戳为指定格式（MM/dd mm:ss)
            tsdbHandler.handleData(tempList, timeList, temperature);
            tsdbHandler.handleData(humpList, timeList, humidity);

            humitureVO.setHumidity(humidity)
                    .setTemperature(temperature)
                    .setTime(timeList);

            map.put("humiture",humitureVO);

            //单个设备数据
            HumitureDeviceVO humitureDataVO = new HumitureDeviceVO();
            List<Result> results = tsdbService.getDataForDevice(TsdbConstant.METRIC_TEMP,TsdbConstant.TAG_VALUE2);
            humitureDataVO.setTemperature(tsdbHandler.handleHumitureData(results));
            //处理温度状态
            HumStatusDTO humStatusDTO = tsdbHandler.handleStatus(results);

            results = tsdbService.getDataForDevice(TsdbConstant.METRIC_HUM,TsdbConstant.TAG_VALUE2);
            humitureDataVO.setHumidity(tsdbHandler.handleHumitureData(results));

            humitureDataVO.setName("示例设备");
            humitureDataVO.setStatus(humStatusDTO.getStatus());
            humitureDataVO.setImgUrl(humStatusDTO.getImgUrl());

            map.put("device",humitureDataVO);

        }catch (Exception e){
            log.info("获取示例数据出错{}",e.getMessage());
            return R.error();
        }

        return R.ok(map);
    }




}
