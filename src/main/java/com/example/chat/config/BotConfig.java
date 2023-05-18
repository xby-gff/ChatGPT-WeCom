package com.example.chat.config;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xingboyuan
 * @date 2023/5/6 8:55
 */
@Slf4j
@Data
@Component
public class BotConfig {

    @Resource
    ProxyConfig proxyConfig;

    @Resource
    WeComConfig weComConfig;

    @Resource
    ChatGPTConfig chatgptConfig;

//    private Map<String, String> weComDict;
    private List<OpenAiService> openAiServiceList;
    private ChatMessage basicPrompt;
    @Value("${chatgpt.maxToken}")
    private Integer maxToken;
    @Value("${chatgpt.temperature}")
    private Double temperature;
    @Value("${chatgpt.model}")
    private String model;

    @PostConstruct
    public void init() {
        //配置代理
        if (null != proxyConfig.getHost() && !"".equals(proxyConfig.getHost())) {
            System.setProperty("http.proxyHost", proxyConfig.getHost());
            System.setProperty("https.proxyHost", proxyConfig.getHost());
        }
        if (null != proxyConfig.getPort() && !"".equals(proxyConfig.getPort())) {
            System.setProperty("http.proxyPort", proxyConfig.getPort());
            System.setProperty("https.proxyPort", proxyConfig.getPort());
        }

//        List<String> list = weComConfig.getDict();
//        weComDict = getMap(list);
        openAiServiceList = new ArrayList<>();
        for (String apiKey : chatgptConfig.getApiKey()) {
            apiKey = apiKey.trim();
            if (!"".equals(apiKey)) {
                openAiServiceList.add(new OpenAiService(apiKey, Duration.ofSeconds(1000)));
                log.info("apiKey为 {} 的账号初始化成功", apiKey);
            }
        }

        System.out.println(model);

    }

    //写一个方法，入参是List<String>,遍历这个List,把=的左右两边的值放到一个Map里面，返回这个Map
    public Map<String, String> getMap(List<String> list) {
        Map<String, String> map = new HashMap<>();
        for (String s : list) {
            String[] split = s.split("=");
            map.put(split[0], split[1]);
        }
        return map;
    }
}
