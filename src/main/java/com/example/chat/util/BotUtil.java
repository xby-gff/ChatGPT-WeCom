package com.example.chat.util;

import com.example.chat.config.BotConfig;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author xingboyuan
 * @date 2023/5/6 9:14
 */
@Component
public class BotUtil {
    @Resource
    public void setAccountConfig(BotConfig botConfig) {
        BotUtil.botConfig = botConfig;
    }

    private static BotConfig botConfig;

    private static final Map<String, List<ChatMessage>> PROMPT_MAP = new HashMap<>();
    private static final Map<OpenAiService, Integer> COUNT_FOR_OPEN_AI_SERVICE = new HashMap<>();
    private static ChatCompletionRequest.ChatCompletionRequestBuilder completionRequestBuilder;

    @PostConstruct
    public void init() {
        completionRequestBuilder = ChatCompletionRequest.builder().model(botConfig.getModel()).temperature(botConfig.getTemperature()).maxTokens(botConfig.getMaxToken());
        for (OpenAiService openAiService : botConfig.getOpenAiServiceList()) {
            COUNT_FOR_OPEN_AI_SERVICE.put(openAiService, 0);
        }
    }


    //根据key获取openAiService
    public static OpenAiService getOpenAiService() {
        //获取使用次数最小的openAiService 否则获取map中的第一个
        Optional<OpenAiService> openAiServiceToUse = COUNT_FOR_OPEN_AI_SERVICE.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
        if (openAiServiceToUse.isPresent()) {
            COUNT_FOR_OPEN_AI_SERVICE.put(openAiServiceToUse.get(), COUNT_FOR_OPEN_AI_SERVICE.get(openAiServiceToUse.get()) + 1);
            return openAiServiceToUse.get();
        } else {
            COUNT_FOR_OPEN_AI_SERVICE.put(COUNT_FOR_OPEN_AI_SERVICE.keySet().iterator().next(), COUNT_FOR_OPEN_AI_SERVICE.get(COUNT_FOR_OPEN_AI_SERVICE.keySet().iterator().next()) + 1);
            return COUNT_FOR_OPEN_AI_SERVICE.keySet().iterator().next();
        }
    }

    public static ChatCompletionRequest.ChatCompletionRequestBuilder getCompletionRequestBuilder() {
        return completionRequestBuilder;
    }

    public static List<ChatMessage> buildPrompt(String sessionId, String newPrompt) {
        if (!PROMPT_MAP.containsKey(sessionId)) {
            if (null != botConfig.getBasicPrompt()){
                List<ChatMessage> promptList = new ArrayList<>();
                promptList.add(botConfig.getBasicPrompt());
                PROMPT_MAP.put(sessionId, promptList);
            }
        }
        List<ChatMessage> promptList = PROMPT_MAP.getOrDefault(sessionId, new ArrayList<>());
        promptList.add(new ChatMessage("user", newPrompt));
        return promptList;
    }

    public static void updatePrompt(String sessionId, List<ChatMessage> promptList) {
        PROMPT_MAP.put(sessionId, promptList);
    }

    public static boolean isPromptEmpty(String sessionId){
        if (!PROMPT_MAP.containsKey(sessionId)){
            return true;
        }
        List<ChatMessage> promptList = PROMPT_MAP.get(sessionId);
        if (null != botConfig.getBasicPrompt()){
            return promptList.size() == 1;
        }else {
            return promptList.size() == 0;
        }
    }

    public static boolean deleteFirstPrompt(String sessionId) {
        if (!isPromptEmpty(sessionId)){
            int index = null != botConfig.getBasicPrompt() ? 1 : 0;
            List<ChatMessage> promptList = PROMPT_MAP.get(sessionId);
            //问
            promptList.remove(index);
            //答
            promptList.remove(index);
            updatePrompt(sessionId, promptList);
            return true;
        }
        return false;
    }

    public static void resetPrompt(String sessionId) {
        PROMPT_MAP.remove(sessionId);
    }
}
