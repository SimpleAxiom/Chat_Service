package edu.msudenver.chat.privateMessage;


import com.fasterxml.jackson.annotation.JsonProperty;
import edu.msudenver.chat.message.Message;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "private_messages")
@IdClass(PrivateMessageId.class)
public class PrivateMessage {

    @Id
    @Column(name = "message_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long messageId;

    @ManyToOne()
    @JoinColumn(name = "message_id", referencedColumnName = "message_id", insertable = false, updatable = false)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Message message;


    @Id
    @Column(name = "receiver_id")
    private Long receiverId;

    //@Id
    //@Column(name = "local_date_time")
    //private Long ldt;

    public PrivateMessage() {
    }

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

    public Long getReceiver_id() {
        return receiverId;
    }

    public void setReceiver_id(Long receiver_id) {
        this.receiverId = receiver_id;
    }


    //public Long getMessageLdt() { return ldt;}
    //public void setMessageLdt(Long ldt) {this.ldt = ldt;}

}
