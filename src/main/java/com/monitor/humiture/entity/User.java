package com.monitor.humiture.entity;

import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@Data
public class User {

    private Integer id;

    private String openId;

    private String nickName;

    private String gender;

    private String city;

    private String province;

    private String country;

    private String avatarUrl;

    private String phone;



    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
