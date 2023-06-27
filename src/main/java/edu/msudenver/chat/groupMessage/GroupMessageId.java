package edu.msudenver.chat.groupMessage;

import java.io.Serializable;

public class GroupMessageId implements Serializable {
    private Long groupId;
    private Long messageId;

    public GroupMessageId(Long groupId, Long messageId) {
        this.groupId = groupId;
        this.messageId = messageId;
    }

    public GroupMessageId() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}

