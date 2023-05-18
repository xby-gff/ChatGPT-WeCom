package com.example.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xingboyuan
 * @date 2023/5/6 9:09
 */
@Data
@Component
@ConfigurationProperties("chatgpt")
public class ChatGPTConfig {
    private List<String> apiKey;
    private Integer maxToken;
    private Double temperature;
    private String model;
}
