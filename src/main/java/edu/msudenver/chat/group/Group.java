package edu.msudenver.chat.group;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @Column(name = "group_id", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_creation_date")
    private LocalDateTime groupCreationDate;

    public Group(Long groupId, String groupName, LocalDateTime groupCreationDate) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupCreationDate = groupCreationDate;
    }

    public Group() {
    }

    public Long getGroupID() {
        return groupId;
    }

    public void setGroupID(Long groupID) {
        this.groupId = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getGroupCreationDate() {
        return groupCreationDate;
    }

    public void setGroupCreationDate(LocalDateTime groupCreationDate) {
        this.groupCreationDate = groupCreationDate;
    }
}
