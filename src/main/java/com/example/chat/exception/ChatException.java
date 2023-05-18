package com.example.chat.exception;

/**
 * @author xingboyuan
 * @date 2023/5/6 9:39
 */
public class ChatException extends Exception{
    public ChatException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}