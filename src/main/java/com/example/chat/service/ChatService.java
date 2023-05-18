package com.example.chat.service;

import com.example.chat.entity.ChatInfo;
import com.example.chat.exception.ChatException;

public interface ChatService {

    String chat(String toUserName,String prompt) throws ChatException;
}
