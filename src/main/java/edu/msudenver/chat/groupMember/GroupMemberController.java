package edu.msudenver.chat.groupMember;

import edu.msudenver.chat.group.Group;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/chat/groups/{groupId}/members")
public class GroupMemberController {
    @Autowired
    private GroupMemberService groupMemberService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<GroupMember>> getGroupMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupMemberService.getGroupMembers());
    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<GroupMember> createGroupMember(@PathVariable Long groupId,@RequestBody GroupMember groupMember) {
        try {
            groupMember.setGroupId(groupId);
            return new ResponseEntity<>(groupMemberService.saveGroupMember(groupMember), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{playerId}")
    public ResponseEntity<Void> deleteGroupMember(@PathVariable Long groupId, @PathVariable Long playerId) {
        return new ResponseEntity<>(groupMemberService.deleteGroupMember(groupId, playerId) ?
                HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}

