package com.monitor.humiture.handler;

import com.baidubce.services.tsdb.model.Group;
import com.baidubce.services.tsdb.model.Result;
import com.monitor.humiture.constant.HumStatusEnum;
import com.monitor.humiture.dto.HumStatusDTO;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TsdbHandler {

    /**
     * 处理tsdb时间戳  --- 将时间戳转换为 "MM/dd HH:mm"
     * @param timeList
     * @return
     */
    public List<String> handleTime(List<Long> timeList) {

        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm");

        List<String> list = new ArrayList<>();
        for(Long time:timeList){
            String date = format.format(new Date(time));
            list.add(date);
        }

        return list;
    }


    public  String handleTime(Long time){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm");
        return format.format(new Date(time));
    }


    public  void handleData(List<Result> resultList, List<String> timeList, List<Double> data) {
        for(Result result: resultList){
            for(Group group:result.getGroups()){
                try {
                    for(Group.TimeAndValue timeAndValue:group.getTimeAndValueList()){
                        if(timeAndValue.isDouble()){
                            System.out.println("\t\t\t[" + timeAndValue.getTime() + "," + timeAndValue.getDoubleValue() + "]");
                            if(timeList.size() < group.getTimeAndValueList().size()){
                                //格式化时间戳为指定格式（MM/dd mm:ss)
                                timeList.add(handleTime(timeAndValue.getTime()) );
                            }
                            //处理数据，保留小数点后两位
                            DecimalFormat df = new DecimalFormat("#.##");
                            data.add(Double.valueOf(df.format(timeAndValue.getDoubleValue())));

                        }else if (timeAndValue.isLong()) {
                            System.out.println("数据点类型为Long");
                            System.out.println("\t\t\t[" + timeAndValue.getTime() + "," + timeAndValue.getLongValue() + "]");
                        } else {
                            System.out.println("数据点类型为其它类型");
                            System.out.println("\t\t\t[" + timeAndValue.getTime() + "," + timeAndValue.getStringValue() + "]");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理温湿度数据
     * @param resultList
     * @return
     */
    public Double handleHumitureData(List<Result> resultList){

        for(Result result: resultList){
            for(Group group:result.getGroups()){
                try {
                    for(Group.TimeAndValue timeAndValue:group.getTimeAndValueList()){
                        if(timeAndValue.isDouble()){
                            System.out.println("\t\t\t[" + timeAndValue.getTime() + "," + timeAndValue.getDoubleValue() + "]");
                            DecimalFormat df = new DecimalFormat("#.##");
                            return Double.valueOf(df.format(timeAndValue.getDoubleValue()));
                       }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    /**
     * 处理温湿度状态
     * @param results
     * @return
     */
    public HumStatusDTO handleStatus(List<Result> results) {

        HumStatusDTO humStatusDTO = new HumStatusDTO();

        for(Result result: results){
            for(Group group:result.getGroups()){
                try {
                    for(Group.TimeAndValue timeAndValue:group.getTimeAndValueList()){

                        double value = timeAndValue.getDoubleValue();

                        if(value>=36){
                            humStatusDTO.setStatus(HumStatusEnum.ExtremelyHot.getStatus());
                            humStatusDTO.setImgUrl(HumStatusEnum.ExtremelyHot.getImgUrl());
                        }else if(value>=31 && value<=35){
                            humStatusDTO.setStatus(HumStatusEnum.ScorchingHot.getStatus());
                            humStatusDTO.setImgUrl(HumStatusEnum.ScorchingHot.getImgUrl());
                        }else if(value>=25 && value<=30){
                            humStatusDTO.setStatus(HumStatusEnum.Hotness.getStatus());
                            humStatusDTO.setImgUrl(HumStatusEnum.Hotness.getImgUrl());
                        }else if(value>=18 && value<=24){
                            humStatusDTO.setStatus(HumStatusEnum.COOL.getStatus());
                            humStatusDTO.setImgUrl(HumStatusEnum.COOL.getImgUrl());
                        }else if(value>=1 && value<=17){
                            humStatusDTO.setStatus(HumStatusEnum.COLD_AND_CLEAR.getStatus());
                            humStatusDTO.setImgUrl(HumStatusEnum.COLD_AND_CLEAR.getImgUrl());
                        }else if(value>=-10 && value<=0){
                            humStatusDTO.setStatus(HumStatusEnum.COLD.getStatus());
                            humStatusDTO.setImgUrl(HumStatusEnum.COLD.getImgUrl());
                        }else if(value>=-20 && value<=-11){
                            humStatusDTO.setStatus(HumStatusEnum.SevereCold.getStatus());
                            humStatusDTO.setImgUrl(HumStatusEnum.SevereCold.getImgUrl());
                        }else{
                            humStatusDTO.setStatus(HumStatusEnum.ABYSM.getStatus());
                            humStatusDTO.setImgUrl(HumStatusEnum.ABYSM.getImgUrl());
                        }

                        return humStatusDTO;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return humStatusDTO;



    }
}
