package com.monitor.humiture.service.impl;

import com.baidubce.services.tsdb.TsdbClient;
import com.baidubce.services.tsdb.TsdbConstants;
import com.baidubce.services.tsdb.model.*;
import com.monitor.humiture.constant.TsdbConstant;
import com.monitor.humiture.service.TsdbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class TsdbServiceImpl implements TsdbService {

    @Autowired
    private TsdbClient tsdbClient;


    @Override
    public List<Result> queryData(String metric, String field, long start, long end, String tagName, String tagValue, String sampLing) {

        // 构造查询对象
        List<Query> queries = Arrays.asList(new Query()  // 创建Query对象
                .withMetric(metric)          // 设置metric
                .withField(field)           // 设置查询的域名，不设置表示查询默认域
                .withFilters(new Filters()     // 创建Filters对象
                        .withAbsoluteStart(start)  //设置绝对的开始时间
                        .withAbsoluteEnd(end) //设置绝对的结束时间
                        .addTag(tagName, tagValue))
                .withLimit(6*24)   // 设置返回数据点数目限制
                .addAggregator(new Aggregator()   // 创建Aggregator对象
                        .withName(TsdbConstants.AGGREGATOR_NAME_AVG)  // 设置聚合函数为Avg
                        .withSampling(sampLing)));  // 设置采样  --- 聚合函数的计算范围

        // 查询数据
        QueryDatapointsResponse response = tsdbClient.queryDatapoints(queries);



        return response.getResults();
    }

    @Override
    public List<Result> queryData(String metric,String tagValue, String sampLing) {

        Date dateEnd = new Date();
        long end = dateEnd.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateEnd);

        calendar.add(Calendar.HOUR_OF_DAY,-24);
        long start = calendar.getTime().getTime();


        // 构造查询对象
        List<Query> queries = Arrays.asList(new Query()  // 创建Query对象
                .withMetric(metric)          // 设置metric
                .withField(TsdbConstant.Filed)           // 设置查询的域名，不设置表示查询默认域
                .withFilters(new Filters()     // 创建Filters对象
                        .withAbsoluteStart(start)  //设置绝对的开始时间
                        .withAbsoluteEnd(end) //设置绝对的结束时间
                        .addTag(TsdbConstant.TAG_NAME, tagValue))
                .withLimit(100)   // 设置返回数据点数目限制
                .addAggregator(new Aggregator()   // 创建Aggregator对象
                        .withName(TsdbConstants.AGGREGATOR_NAME_AVG)  // 设置聚合函数为Avg
                        .withSampling(sampLing)));  // 设置采样  --- 聚合函数的计算范围

        // 查询数据
        QueryDatapointsResponse response = tsdbClient.queryDatapoints(queries);


        return response.getResults();
    }

    @Override
    public Map<String, List<String>> getTags(String metric) {
        // 获取标签
        GetTagsResponse response = tsdbClient.getTags(metric);
        return response.getTags();
    }

    @Override
    public List<Map<String, List<String>>> getAllTags() {
        //暂不考虑
        return null;
    }

    @Override
    public Map<String,List<String>> getTagValueForDevice() {

        //存储一个metric对应多个value
        Map<String,List<String>> mapList = new HashMap<>();

        //获取metrics
        List<String> metrics = getMetrics();
        for(String metric:metrics){
            //获取对应的tag
            Map<String, List<String>>  map = getTags(metric);
            for(Map.Entry<String, List<String>> entry : map.entrySet()){
                if(TsdbConstant.TAG_NAME.equals(entry.getKey())){
                    mapList.put(metric,entry.getValue());
                    break;
                }
            }
        }

        return mapList;
    }


    @Override
    public List<Result> getDataForDevice(String metric, String deviceId) {

        try {

            Filters filters = null;
            if(TsdbConstant.TAG_VALUE2.equals(deviceId) || TsdbConstant.TAG_VALUE1.equals(deviceId)){
                //获取demo数据示例
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateEnd = sdf.parse("2018-08-10 18:00:00");
                long start = dateEnd.getTime();

                Date dateStart = sdf.parse("2018-08-10 18:10:00");
                long end = dateStart.getTime();

                filters = new Filters()
                            .withAbsoluteStart(start)  //设置绝对的开始时间
                            .withAbsoluteEnd(end)   //设置绝对的结束时间
                            .addTag(TsdbConstant.TAG_NAME, deviceId);
            }else {
                //  用户当前时间内的数据（单个）
                String start = TsdbConstant.SAMPLING_10MINUTE;
                String end = TsdbConstant.SAMPLING_SECOND;

                filters = new Filters()
                        .withRelativeStart(start)    //相对时间
                        .withRelativeEnd(end)
                        .addTag(TsdbConstant.TAG_NAME, deviceId);
            }

            // 构造查询对象
            List<Query> queries = Arrays.asList(new Query()  // 创建Query对象
                    .withMetric(metric)          // 设置metric
                    .withField(TsdbConstant.Filed)           // 设置查询的域名，不设置表示查询默认域
                    .withFilters(filters)
                    .withLimit(1)   // 设置返回数据点数目限制
                    .addAggregator(new Aggregator()   // 创建Aggregator对象
                            .withName(TsdbConstants.AGGREGATOR_NAME_AVG)  // 设置聚合函数为Avg
                            .withSampling(TsdbConstant.SAMPLING_10MINUTE)));

            // 查询数据
            QueryDatapointsResponse response = tsdbClient.queryDatapoints(queries);

            return response.getResults();

        }catch (Exception e){
            log.debug("获取设备数据错误:{}",e);
        }

        return null;
    }

    @Override
    public List<String> getMetrics() {

        GetMetricsResponse response = tsdbClient.getMetrics();
        return response.getMetrics();
    }
}
