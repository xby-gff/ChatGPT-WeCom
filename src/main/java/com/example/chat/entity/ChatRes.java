package com.example.chat.entity;

/**
 * @author xingboyuan
 * @date 2023/5/6 13:44
 */

import lombok.Data;

@Data
public class ChatRes {
    /**
     *  {
     *         "msgtype": "text",
     *         "text": {
     *             "content": "123"
     *         }
     *    }
     * 把这个结构转成实体类
     */
    private String msgtype;
    private Text text;

    @Data
    public static class Text {
        private String content;
    }

    public ChatRes(){

    }

    public ChatRes(String content){
        this.msgtype = "text";
        this.text = new Text();
        this.text.content = content;
    }
}
