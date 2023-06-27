package edu.msudenver.chat.groupMember;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.msudenver.chat.group.Group;

import javax.persistence.*;

@Entity
@Table(name = "group_members")
@IdClass(GroupMemberId.class)
public class GroupMember {

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    @Id
    @Column(name = "group_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long groupId;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Group group;

    @Id
    @Column(name = "player_id")
    private Long playerId;


    //@JoinColumn(name = "group_name", referencedColumnName = "group_name", insertable = false, updatable = false)
    //private String playerName;

    public GroupMember(Group group, Long playerID) {
        this.group = group;
        this.playerId = playerID;
    }

    public GroupMember() {
    }


}
