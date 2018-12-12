package com.monitor.humiture.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
//@ConfigurationProperties(prefix = "tsdb")
public class TsdbClientProperties {

    @Value("${tsdb.acessKey}")
    private String acessKey;

    @Value("${tsdb.secret}")
    private String secret;

    @Value("${tsdb.adminEndpoint}")
    private String adminEndpoint;

    @Value("${tsdb.endpoint}")
    private String endpoint;

}
