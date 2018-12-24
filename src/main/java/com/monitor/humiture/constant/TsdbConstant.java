package com.monitor.humiture.constant;

public interface TsdbConstant {

    String METRIC_HUM = "humidity";

    String METRIC_TEMP = "temperature";

    String Filed = "value";

    String TAG_NAME = "device";

    String TAG_VALUE1 = "x1";

    String TAG_VALUE2 = "y1";

    /**
     * 采样时间范围：1s 1min 10min 1h 1day
     */
    String SAMPLING_SECOND = "1 second";
    String SAMPLING_MINUTE = "1 minute";
    String SAMPLING_10MINUTE = "10 minute";
    String SAMPLING_HOUR = "1 hour";
    String SAMPLING_DAY = "1 day";

    /**
     *
     */


}
