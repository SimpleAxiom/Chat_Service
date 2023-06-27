package edu.msudenver.chat.privateMessage;

import edu.msudenver.chat.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, PrivateMessageId> {

    List<PrivateMessage> findByReceiverId(Long receiverId);

    @Query("select u from PrivateMessage u where u.receiverId = :receiverId and u.message.dateTime > :ldt")
    List<PrivateMessage> findLatestPrivateMessages(Long receiverId, LocalDateTime ldt);

    @Query("select u from PrivateMessage u where u.receiverId = :receiverId and u.message.dateTime > :ldt and u.message.messageType = 'AREA'")
    List<PrivateMessage> findLatestAreaMessages(Long receiverId, LocalDateTime ldt);
}
