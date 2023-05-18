package com.example.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author xingboyuan
 * @date 2023/5/6 8:59
 */
@Data
@Component
@ConfigurationProperties("wecom")
public class WeComConfig {
//    private List<String> dict;
//    private String url;

    private String sToken;
    private String sCorpID ;
    private String sEncodingAESKey ;
}
