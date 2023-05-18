package com.example.chat.entity;

import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author xingboyuan
 * @date 2023/5/6 15:26
 */
@XmlRootElement(name = "xml")
@ToString
public class WeComDTO implements Serializable {

    private static final long serialVersionUID = 10002L;

    @XmlElement(name = "ToUserName")
    private String ToUserName;

    @XmlElement(name = "AgentID")
    private String AgentID;

    @XmlElement(name = "Encrypt")
    private String Encrypt;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public String getEncrypt() {
        return Encrypt;
    }

    public void setEncrypt(String encrypt) {
        Encrypt = encrypt;
    }
}
