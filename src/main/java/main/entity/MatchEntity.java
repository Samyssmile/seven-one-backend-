package main.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Entity
public class MatchEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @Column(name = "uuid", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;


    @OneToOne
    @JoinColumn(name = "first_team_uuid")
    private TeamEntity firstTeam;

    @OneToOne
    @JoinColumn(name = "second_team_uuid")
    private TeamEntity secondTeam;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date matchDate;
    @Column
    private String groupName;
    @Column
    private String result;

    public TeamEntity getSecondTeam() {
        return secondTeam;
    }

    public MatchEntity(UUID uuid, TeamEntity firstTeam, TeamEntity secondTeam, Date matchDate, String groupName, String result) {
        this.uuid = uuid;
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.matchDate = matchDate;
        this.groupName = groupName;
        this.result = result;
    }

    public void setSecondTeam(TeamEntity secondTeam) {
        this.secondTeam = secondTeam;
    }


    public TeamEntity getFirstTeam() {
        return firstTeam;
    }

    public void setFirstTeam(TeamEntity firstTeam) {
        this.firstTeam = firstTeam;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String group) {
        this.groupName = group;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public MatchEntity() {
    }


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Transactional
    public static void persistMatch(MatchEntity matchEntity) {
        persist(matchEntity);
    }
}
