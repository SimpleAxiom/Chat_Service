package edu.msudenver.chat.groupMessage;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface GroupMessageRepository extends JpaRepository<GroupMessage, GroupMessageId> {
    List<GroupMessage> findByGroupId(Long groupId);

    @Query("select u from GroupMessage u where u.groupId = :groupId and u.message.dateTime > :ldt")
    List<GroupMessage> findLatestGroupMessages(Long groupId, LocalDateTime ldt);

    @Query("select u from GroupMessage u where u.groupId = :groupId and u.message.dateTime > :ldt and u.message.messageType = 'AREA'")
    List<GroupMessage> findLatestAreaMessages(Long groupId, LocalDateTime ldt);
}
