package com.example.chat.service.impl;

import com.example.chat.entity.ChatInfo;
import com.example.chat.exception.ChatException;
import com.example.chat.service.ChatService;
import com.example.chat.util.ChatGPTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



/**
 * @author xingboyuan
 * @date 2023/5/6 9:36
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Override
    public String chat(String toUserName,String prompt) throws ChatException {

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setPrompt(prompt);
        chatInfo.setSessionId(toUserName);
        log.info("村曰提问===>{}:{}",toUserName,prompt);
        String result = ChatGPTUtil.chat(chatInfo);
        log.info("GPT回复===>{}",result);
        return result;
    }
}
