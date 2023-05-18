package com.example.chat.entity;

import lombok.Data;

/**
 * @author xingboyuan
 * @date 2023/5/9 9:48
 */
@Data
public class WeComResponse {

    private String ToUserName;

    private String FromUserName;

    private String CreateTime;

    private String MsgType;

    private String Content;
}
