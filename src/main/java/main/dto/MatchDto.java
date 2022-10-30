package main.dto;

import java.util.Date;
import java.util.UUID;

public class MatchDto {
    private UUID uuid;
    private TeamDto firstTeam;
    private TeamDto secondTeam;
    private Date matchDate;
    private String groupName;
    private String result;

    public MatchDto() {
    }

    public MatchDto(UUID uuid, TeamDto firstTeam, TeamDto secondTeam, Date matchDate, String groupName, String result) {
        this.uuid = uuid;
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.matchDate = matchDate;
        this.groupName = groupName;
        this.result = result;
    }

    public MatchDto(TeamDto firstTeam, TeamDto secondTeam, Date matchDate, String groupName, String result) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.matchDate = matchDate;
        this.groupName = groupName;
        this.result = result;
    }

    public TeamDto getFirstTeam() {
        return firstTeam;

    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setFirstTeam(TeamDto firstTeam) {
        this.firstTeam = firstTeam;
    }

    public TeamDto getSecondTeam() {
        return secondTeam;
    }

    public void setSecondTeam(TeamDto secondTeam) {
        this.secondTeam = secondTeam;
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

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
