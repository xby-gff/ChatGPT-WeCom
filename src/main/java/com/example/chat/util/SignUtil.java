package com.example.chat.util;

import com.alibaba.fastjson.JSONObject;
import com.example.chat.config.WeComConfig;
import com.example.chat.entity.WeComDTO;
import com.example.chat.util.aes.AesException;
import com.example.chat.util.aes.WXBizJsonMsgCrypt;
import com.example.chat.util.aes.xml.WXBizMsgCrypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xingboyuan
 * @date 2023/5/6 10:12
 */
@Component
public class SignUtil {

    @Resource
    WeComConfig weComConfig;

    public String VerifyURL(String msg_signature, String timestamp, String nonce, String echostr)throws AesException {
        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(weComConfig.getSToken(), weComConfig.getSEncodingAESKey(), weComConfig.getSCorpID());
        String sEchoStr;
        try {
            sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp,nonce, echostr);
            System.out.println("verifyurl echostr: " + sEchoStr);
            // 验证URL成功，将sEchoStr返回
            // HttpUtils.SetResponse(sEchoStr);
            return sEchoStr;
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
            return null;
        }
    }

    public String DecryptMsg(String msg_signature,String timestamp,String nonce,WeComDTO body) throws  com.example.chat.util.aes.xml.AesException {
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(weComConfig.getSToken(), weComConfig.getSEncodingAESKey(), weComConfig.getSCorpID());
        String msg = body.getEncrypt();
        String xmlcontent = wxcpt.decrypt(msg);

        return xmlcontent;
    }
}
