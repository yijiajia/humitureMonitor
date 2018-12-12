package com.monitor.humiture.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String code;

    private String rawData;

    private String signature;
}
