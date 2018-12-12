package com.monitor.humiture.config;

import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.tsdb.TsdbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableConfigurationProperties(TsdbClientProperties.class)
public class TsdbClientConfig {

    @Autowired
    private TsdbClientProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public TsdbClient tsdbClient(){
        //创建配置
        BceClientConfiguration configuration = new BceClientConfiguration()
                .withCredentials(new DefaultBceCredentials(properties.getAcessKey(), properties.getSecret()))
                .withEndpoint(properties.getEndpoint());

        //初始化一个tsdbClient
        return new TsdbClient(configuration);
    }
}
