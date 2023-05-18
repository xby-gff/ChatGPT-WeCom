package com.example.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xingboyuan
 * @date 2023/5/6 8:54
 */
@Data
@Component
@ConfigurationProperties("proxy")
public class ProxyConfig {
    private String host;
    private String port;
}
