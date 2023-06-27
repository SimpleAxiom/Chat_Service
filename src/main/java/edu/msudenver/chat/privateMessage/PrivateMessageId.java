package edu.msudenver.chat.privateMessage;

import java.io.Serializable;

public class PrivateMessageId implements Serializable {
    private Long receiverId;
    private Long messageId;

    public PrivateMessageId(Long receiverId, Long messageId) {
        this.receiverId = receiverId;
        this.messageId = messageId;
    }

    public PrivateMessageId() {
    }

    public Long getReceiverID() {
        return receiverId;
    }

    public void setPrivateMessageId(Long privateMessageId) {
        this.receiverId = receiverId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}

