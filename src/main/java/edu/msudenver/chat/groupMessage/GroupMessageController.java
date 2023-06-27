package edu.msudenver.chat.groupMessage;

import edu.msudenver.chat.message.Message;
import edu.msudenver.chat.message.MessageService;
import edu.msudenver.chat.groupMessage.GroupMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping(path = "/chat/groups/{groupId}/messages")
public class GroupMessageController {
    @Autowired
    private GroupMessageService groupMessageService;

    @Autowired
    private MessageService messageService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<GroupMessage>> getGroupMessages(@PathVariable Long groupId, @RequestParam(required = false) Long epoch) {

        List<GroupMessage> groupMessage;

        LocalDateTime ldt;
        if(epoch == null) {
            ldt = LocalDateTime.now();
            ldt = ldt.minusSeconds(60);
        }
        else {
            ldt = Instant.ofEpochMilli(epoch)
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        System.out.println(ldt);

        groupMessage = groupMessageService.getLatestGroupMessages(groupId, ldt);

        return new ResponseEntity<>(groupMessage, groupMessage == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<GroupMessage> createGroupMessage(@PathVariable Long groupId,@RequestBody GroupMessage message) {
        try {

            LocalDateTime lt = LocalDateTime.now();
            Message tempMessage = message.getMessage();
            tempMessage.setDateTime(lt);
            tempMessage.setMessageType("GROUP");

            Message updatedMessage = messageService.saveMessage(tempMessage);
            message.setMessageId(updatedMessage.getMessageId());
            message.setMessage(updatedMessage);
            message.setGroupId(groupId);

            return new ResponseEntity<>(groupMessageService.saveGroupMessage(message), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }
    
}

