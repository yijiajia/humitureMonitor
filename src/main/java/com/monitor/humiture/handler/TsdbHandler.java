package com.monitor.humiture.handler;

import com.baidubce.services.tsdb.model.Group;
import com.baidubce.services.tsdb.model.Result;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TsdbHandler {

    /**
     * 处理tsdb时间戳  --- 将时间戳转换为 "MM/dd HH:mm"
     * @param timeList
     * @return
     */
    public static List<String> handleTime(List<Long> timeList) {

        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm");

        List<String> list = new ArrayList<>();
        for(Long time:timeList){
            String date = format.format(new Date(time));
            list.add(date);
        }

        return list;
    }


    public static String handleTime(Long time){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm");
        return format.format(new Date(time));
    }


    public static void handleData(List<Result> resultList, List<String> timeList, List<Double> data) {
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




}
