package main.response;

import main.dto.PredictionDto;
import main.entity.TeamEntity;

import java.util.Date;

public class GameByUserResponse {

    private TeamEntity firstTeam;
    private TeamEntity secondTeam;
    private Date matchDate;
    private String group;
    private String result;
    private PredictionDto predictionDto;

    public GameByUserResponse() {
    }

    public GameByUserResponse(TeamEntity firstTeam, TeamEntity secondTeam, Date matchDate, String group, String result, PredictionDto predictionDto) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.matchDate = matchDate;
        this.group = group;
        this.result = result;
        this.predictionDto = predictionDto;
    }

    public TeamEntity getFirstTeam() {
        return firstTeam;
    }

    public void setFirstTeam(TeamEntity firstTeam) {
        this.firstTeam = firstTeam;
    }

    public TeamEntity getSecondTeam() {
        return secondTeam;
    }

    public void setSecondTeam(TeamEntity secondTeam) {
        this.secondTeam = secondTeam;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public PredictionDto getPredictionDto() {
        return predictionDto;
    }

    public void setPredictionDto(PredictionDto predictionDto) {
        this.predictionDto = predictionDto;
    }
}
