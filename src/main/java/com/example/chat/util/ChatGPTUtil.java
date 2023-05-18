package com.example.chat.util;

import com.example.chat.entity.ChatInfo;
import com.example.chat.exception.ChatException;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author xingboyuan
 * @date 2023/5/6 14:16
 */
@Slf4j
public class ChatGPTUtil {

    public static String chat(ChatInfo chatInfo) throws ChatException {
        List<ChatMessage> prompt = BotUtil.buildPrompt(chatInfo.getSessionId(), chatInfo.getPrompt());

        //向gpt提问
        OpenAiService openAiService = BotUtil.getOpenAiService();
        ChatCompletionRequest.ChatCompletionRequestBuilder completionRequestBuilder = BotUtil.getCompletionRequestBuilder();

        ChatCompletionRequest completionRequest = completionRequestBuilder.messages(prompt).build();
        ChatMessage answer = null;
        try {
            answer = openAiService.createChatCompletion(completionRequest).getChoices().get(0).getMessage();
        }catch (OpenAiHttpException e){
            log.error("向gpt提问失败，提问内容：{}，\n原因：{}\n", chatInfo.getPrompt(), e.getMessage());
            if (429 == e.statusCode){
                throw new ChatException("提问过于频繁/机器人太累啦，休息一下吧");
            }else if(400 == e.statusCode){
                log.warn("尝试删除较前会话记录并重新提问");
                //http400错误，大概率是历史会话太多导致token超出限制
                if (BotUtil.deleteFirstPrompt(chatInfo.getSessionId())){
                    return chat(chatInfo);
                }else {
                    log.warn("删除失败");
                    throw new ChatException("提问太长啦，尝试调高token或者减少提问字数");
                }
            }
        }
        if (null == answer){
            throw new ChatException("GPT可能暂时不想理你");
        }

        prompt.add(answer);
        BotUtil.updatePrompt(chatInfo.getSessionId(), prompt);

        return answer.getContent().trim();
    }
}
