package main.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class GroupEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue
    @Column(name = "uuid", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;
    @Column
    private String groupName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamEntity> teams;

    public GroupEntity() {}


    public GroupEntity(UUID uuid, String groupName, Set<TeamEntity> teams) {
        this.uuid = uuid;
        this.groupName = groupName;
        this.teams = teams;
    }
    public Set<TeamEntity> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamEntity> teams) {
        this.teams = teams;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }



}
