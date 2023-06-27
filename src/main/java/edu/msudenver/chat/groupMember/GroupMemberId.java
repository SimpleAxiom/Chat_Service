package edu.msudenver.chat.groupMember;

import java.io.Serializable;

public class GroupMemberId implements Serializable {
    private Long groupId;
    private Long playerId;

    public GroupMemberId(Long groupId, Long playerId) {
        this.groupId = groupId;
        this.playerId = playerId;
    }

    public GroupMemberId() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}

