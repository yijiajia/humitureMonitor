package com.monitor.humiture.service;

import com.baidubce.services.tsdb.model.Group;
import com.baidubce.services.tsdb.model.Result;

import java.util.List;
import java.util.Map;

public interface TsdbService {

    /**
     * 查询数据点 --- 查找特定时间的数据
     * @param metric  --度量名（humidity，temperature）
     * @param field  --域 （子类别）
     * @param start -- 查询的开始时间 （绝对时间）
     * @param end  -- 查询的结束时间 （绝对时间）
     * @param tagName -- 标签名（device）
     * @param tagValue  -- 标签值(x1,y1)
     * @param sampLing -- 采样范围（使用聚合函数时的取值范围） --- "1 hour" 指一个小时内采样
     */
    List<Result> queryData(String metric, String field, long start, long end, String tagName, String tagValue, String sampLing);

    /**
     * 查询当前时间以内24小时的数据
     * @param metric
     * @param tagValue
     * @param sampLing
     * @return
     */
    List<Result> queryData(String metric,String tagValue, String sampLing);


    /**
     * 查询指定metries中的标签
     * @param metric
     * @return
     */
    Map<String, List<String>> getTags(String metric);

    /**
     * 获取所有标签
     * @return
     */
    List<Map<String, List<String>>> getAllTags();

    /**
     * 获取tag为device的所有deviceId
     * @return
     */
    Map<String,List<String>> getTagValueForDevice();


    /**
     * 获取指定metries中的设备当前温度/湿度
     * @param metric
     * @param deviceId
     * @return
     */
    List<Result> getDataForDevice(String metric,String deviceId);



    /**
     * 获取metries列表
     * @return
     */
    List<String> getMetrics();








}
