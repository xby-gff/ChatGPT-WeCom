package com.example.chat.util;

import com.alibaba.fastjson.JSONObject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author xingboyuan
 * @date 2023/5/6 11:43
 */
@Component
public class SendUtil {

    @Value("${wecom.sCorpID}")
    private String sCorpID ;
    @Value("${wecom.corpSecret}")
    private String corpSecret ;
    @Value("${wecom.agentId}")
    private String agentId ;

    private String WECHAT_TOKEN = "WECHAT_TOKEN";

    public String getAccessToken() throws Exception {

        String data = CacheHelper.get(WECHAT_TOKEN);
        if (data!=null && data!="") {
            return data;
        }
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + sCorpID + "&corpsecret=" + corpSecret;
        String jsonData = OkHttpUtils.get(url);
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        String accessToken = jsonObject.getString("access_token");
        CacheHelper.put(WECHAT_TOKEN, accessToken);
        return accessToken;
    }

    public String sendMsg(String msg, String touser) throws Exception {
        String accessToken = null;
        List<String> msgList = new ArrayList();
        try {
            accessToken = getAccessToken();
        } catch (Exception e) {
            return "fail";
        }
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
        if (msg.length() > 2048) {
            int count = msg.length() / 2048 + 1;
            int beginIndex = 0, endIndex = 2048;
            for (int i = 0; i < count; i++) {
                String temp = msg.substring(beginIndex, endIndex);
                msgList.add(temp);
                beginIndex = endIndex;
                endIndex += 2048;
                endIndex = endIndex > msg.length() ? msg.length() : endIndex;
            }
        } else {
            msgList.add(msg);
        }

        for (String s : msgList) {
            String body = "{\n" +
                    "   \"touser\" : \"" + touser + "\",\n" +
                    "   \"msgtype\" : \"text\",\n" +
                    "   \"agentid\" : " + agentId + ",\n" +
                    "   \"text\" : {\n" +
                    "       \"content\" : \"" + s + "\"\n" +
                    "   },\n" +
                    "   \"safe\":0,\n" +
                    "   \"enable_id_trans\": 0,\n" +
                    "   \"enable_duplicate_check\": 0,\n" +
                    "   \"duplicate_check_interval\": 1800\n" +
                    "}";
            MediaType JSON1 = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON1, body);
            OkHttpUtils.post(url, "", requestBody, new HashMap());
        }
        return "success;";
    }


}
