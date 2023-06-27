package edu.msudenver.chat.group;

import edu.msudenver.chat.groupMember.GroupMemberService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/chat/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Group>> getGroups() {
        return ResponseEntity.ok(groupService.getGroups());
    }

    @GetMapping(path = "/{groupId}", produces = "application/json")
    public ResponseEntity<Group> getGroup(@PathVariable Long groupId) {
        Group group = groupService.getGroup(groupId);
        return new ResponseEntity<>(group, group == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        try {
            //TODO also add to groupmember list this player ID

            //TODO also pull date from either world server OR just from right here
            LocalDateTime lt = LocalDateTime.now();
            group.setGroupCreationDate(lt);
            return new ResponseEntity<>(groupService.saveGroup(group), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{groupId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Group> updateGroup(@PathVariable Long groupId, @RequestBody Group updatedGroup) {
        Group retrievedGroup = groupService.getGroup(groupId);
        if (retrievedGroup != null) {
            retrievedGroup.setGroupName(updatedGroup.getGroupName());
            try {
                return ResponseEntity.ok(groupService.saveGroup(retrievedGroup));
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        //TODO PROBABLY NEED TO ADD IN DELETING ALL MEMBERS FIRST FROM GROUPMEMBER REPOSITORY
        return new ResponseEntity<>(groupService.deleteGroup(groupId) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
