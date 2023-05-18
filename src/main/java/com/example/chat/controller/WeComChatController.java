package com.example.chat.controller;

import com.alibaba.fastjson.JSON;
import com.example.chat.entity.WeComDTO;
import com.example.chat.service.ChatService;
import com.example.chat.util.BotUtil;
import com.example.chat.util.SendUtil;
import com.example.chat.util.SignUtil;
import com.example.chat.util.aes.AesException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * @author xingboyuan
 * @date 2023/5/6 9:58
 */
@RestController
@Slf4j
public class WeComChatController {
    @Autowired
    private ChatService chatService;

    @Resource
    SignUtil signUtil;

    @Resource
    SendUtil sendUtil;

    @Resource
    BotUtil botUtil;


    private Executor executor = Executors.newCachedThreadPool();

    private static final String RESET_WORD = "reset";
    /**
     * 验证接口
     */
    @GetMapping(value = "/chat")
    public void chat(HttpServletRequest request, HttpServletResponse response) throws AesException, UnsupportedEncodingException {
        // 微信加密签名
        String msg_signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        // 如果是刷新，需返回原echostr
        String echostr = request.getParameter("echostr");
        log.info("chat params: msg_signature-{} , timestamp-{} , nonce-{} , echostr-{}",msg_signature,timestamp,nonce, echostr);
        PrintWriter out;
        String sEchoStr = "";

        try {
            sEchoStr = signUtil.VerifyURL(msg_signature, timestamp, nonce, echostr);

            // 验证URL成功，将sEchoStr返回
            //2.3若校验成功，则原样返回 echoStr
            out = response.getWriter();
            out.print(sEchoStr);
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }
    }

    /**
     * 接收消息
     */
    @PostMapping(value = "/chat",
            consumes = {"application/xml", "text/xml"},
            produces = "application/xml;charset=UTF-8")
    public String chat(@RequestParam("msg_signature") String msg_signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestBody WeComDTO body) throws Exception {
        log.info("chat params: msg_signature-{} , timestamp-{} , nonce-{} , body-{}",msg_signature,timestamp,nonce, JSON.toJSONString(body));

        String xmlcontent = signUtil.DecryptMsg(msg_signature,timestamp,nonce,body);
        String content = StringUtils.substringBetween(xmlcontent, "<Content><![CDATA[", "]]></Content>");
        String fromUser = StringUtils.substringBetween(xmlcontent, "<FromUserName><![CDATA[", "]]></FromUserName>");
        log.info("chat params: content-{} , fromUser-{} ",content,fromUser);
        executor.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                if (RESET_WORD.equals(content)){
                    botUtil.resetPrompt(fromUser);
                    sendUtil.sendMsg("Conversation history forgotten.", fromUser);
                }else {
                    String msg = chatService.chat(fromUser, content);
                    sendUtil.sendMsg(msg, fromUser);
                }
            }
        });
       return "success";
    }

    @PostMapping(value = "/chat/cy")
    public String chat( @RequestParam("userId") String userId,
                        @RequestParam("prompt") String prompt) throws Exception {
        String msg = chatService.chat(userId,prompt);
        return msg;
    }
}
