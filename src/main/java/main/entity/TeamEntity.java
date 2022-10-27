package main.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;
@Entity
public class TeamEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @Column(name = "uuid")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;
    @Column
    private String teamName;
    @Column
    private String img;

    @ManyToOne
    @JoinColumn(name = "group_uuid")
    private GroupEntity groupEntity;

    public TeamEntity(String teamName, String img) {
        this.teamName = teamName;
        this.img = img;
    }

    public TeamEntity(UUID uuid, String teamName, String img, GroupEntity groupEntity) {
        this.uuid = uuid;
        this.teamName = teamName;
        this.img = img;
        this.groupEntity = groupEntity;
    }

    public TeamEntity() {
    }

    public GroupEntity getGroupEntity() {
        return groupEntity;
    }

    public void setGroupEntity(GroupEntity groupEntity) {
        this.groupEntity = groupEntity;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}

