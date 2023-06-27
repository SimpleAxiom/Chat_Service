package edu.msudenver.chat.privateMessage;

import edu.msudenver.chat.message.Message;
import edu.msudenver.chat.message.MessageService;
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
@RequestMapping(path = "/chat/private")
public class PrivateMessageController {
    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private MessageService messageService;

    @GetMapping(path = "/{playerId}", produces = "application/json")
    public ResponseEntity<List<PrivateMessage>> getPrivateMessages(@PathVariable Long playerId,
                                                                   @RequestParam(required = false) Long epoch) {

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
        List<PrivateMessage> privateMessage = privateMessageService.getLatestPrivateMessages(playerId, ldt);

        return new ResponseEntity<>(privateMessage, privateMessage == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<PrivateMessage>> getPrivateMessages() {
        List<PrivateMessage> privateMessage = privateMessageService.getPrivateMessages();
        return new ResponseEntity<>(privateMessage, privateMessage == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<PrivateMessage> createPrivateMessage(@RequestBody PrivateMessage message) {
        try {
            LocalDateTime lt = LocalDateTime.now();
            Message tempMessage = message.getMessage();
            tempMessage.setDateTime(lt);
//            group.setGroupCreationDate(lt);

            Message updatedMessage = messageService.saveMessage(tempMessage);
            message.setMessageId(updatedMessage.getMessageId());
            message.setMessage(updatedMessage);

            return new ResponseEntity<>(privateMessageService.savePrivateMessage(message), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }
}
