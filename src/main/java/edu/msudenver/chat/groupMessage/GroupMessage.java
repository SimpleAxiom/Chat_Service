package edu.msudenver.chat.groupMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.msudenver.chat.message.Message;

import javax.persistence.*;

@Entity
@Table(name = "group_messages")
@IdClass(GroupMessageId.class)
public class GroupMessage {

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Id
    @Column(name = "message_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long messageId;

    @ManyToOne()
    @JoinColumn(name = "message_id", referencedColumnName = "message_id", insertable = false, updatable = false)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Message message;

    @Id
    @Column(name = "group_id")
    private Long groupId;

    public GroupMessage(Long groupId, Long messageId,  Message message) {
        this.groupId = groupId;
        this.messageId = messageId;
        this.message = message;
    }

    public GroupMessage() {

    }
}
