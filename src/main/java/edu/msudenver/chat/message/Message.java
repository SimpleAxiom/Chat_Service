package edu.msudenver.chat.message;


import com.fasterxml.jackson.annotation.JsonProperty;
import edu.msudenver.chat.groupMessage.GroupMessageId;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {



    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    @Id
    @Column(name = "message_id", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long messageId;


    @Column(name = "message_type")
    private String messageType;

    @Column(name = "date_time")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dateTime;

    @Column(name = "message_body")
    private String messageBody;

    @Column(name = "sender_type")
    public String senderType;

    @Column(name = "sender_id")
    private Long senderId;

    public Message(Long messageId, String messageType, LocalDateTime dateTime, String messageBody, String senderType, Long senderId) {
        this.messageId = messageId;
        this.messageType = messageType;
        this.dateTime = dateTime;
        this.messageBody = messageBody;
        this.senderType = senderType;
        this.senderId = senderId;
    }

    public Message() {
    }

    public Message(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

}
