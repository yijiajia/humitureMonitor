package com.monitor.humiture.vo;

import lombok.Data;

@Data
public class HumitureDeviceVO {

    private String deviceId;

    private Double temperature;

    private Double humidity;

    private String name;

    /**
     * 舒适情况
     */
    private String status;

    /**
     * 状态图
     */
    private String imgUrl;


}
