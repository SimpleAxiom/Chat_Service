package edu.msudenver.chat.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByDateTimeAfter(Date startDate);

    List<Message> findByMessageType(String messageType);
}
