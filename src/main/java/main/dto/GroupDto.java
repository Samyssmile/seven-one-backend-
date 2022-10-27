package main.dto;

import java.util.Set;
import java.util.UUID;

public class GroupDto {
    private UUID uuid;

    private String groupName;
    private Set<TeamDto> teams;

    public GroupDto(UUID uuid, String groupName, Set<TeamDto> teams) {
        this.uuid = uuid;
        this.groupName = groupName;
        this.teams = teams;
    }

    public GroupDto() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public GroupDto(String groupName, Set<TeamDto> teams) {
        this.groupName = groupName;
        this.teams = teams;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<TeamDto> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamDto> teams) {
        this.teams = teams;
    }
}
