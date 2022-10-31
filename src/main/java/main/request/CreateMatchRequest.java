package main.request;

import java.util.Date;
import java.util.UUID;

public class CreateMatchRequest {

    private UUID firstTeamUUID;
    private UUID secondTeamUUID;
    private Date matchDate;
    private String groupName;
    private String result;

    public CreateMatchRequest() {
    }

    public CreateMatchRequest(UUID firstTeamUUID, UUID secondTeamUUID, Date matchDate, String groupName, String result) {
        this.firstTeamUUID = firstTeamUUID;
        this.secondTeamUUID = secondTeamUUID;
        this.matchDate = matchDate;
        this.groupName = groupName;
        this.result = result;
    }

    public UUID getFirstTeamUUID() {
        return firstTeamUUID;
    }

    public void setFirstTeamUUID(UUID firstTeamUUID) {
        this.firstTeamUUID = firstTeamUUID;
    }

    public UUID getSecondTeamUUID() {
        return secondTeamUUID;
    }

    public void setSecondTeamUUID(UUID secondTeamUUID) {
        this.secondTeamUUID = secondTeamUUID;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
