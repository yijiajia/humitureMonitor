package com.monitor.humiture.vo;


import java.util.List;

/**
 * 温湿度数据包装类
 */

public class HumitureVO {

    private List<String> time;

    private List<Double> temperature;

    private List<Double> humidity;

    public List<String> getTime() {
        return time;
    }

    public HumitureVO setTime(List<String> time) {
        this.time = time;
        return this;
    }

    public List<Double> getTemperature() {
        return temperature;
    }

    public HumitureVO setTemperature(List<Double> temperature) {
        this.temperature = temperature;
        return this;
    }

    public List<Double> getHumidity() {
        return humidity;
    }

    public HumitureVO setHumidity(List<Double> humidity) {
        this.humidity = humidity;
        return this;
    }

    public HumitureVO() {
    }
}
