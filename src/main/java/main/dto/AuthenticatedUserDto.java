package main.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class AuthenticatedUserDto {

    private UUID clientUuid;
    private String nickname;
    private int score;
    private int rank;

    private String jwt;

    public AuthenticatedUserDto() {
    }

    public AuthenticatedUserDto(String nickname, int score, int rank) {
        this.nickname = nickname;
        this.score = score;
        this.rank = rank;
    }

    public AuthenticatedUserDto(UUID clientUuid, String nickname, int score, int rank) {
        this.clientUuid = clientUuid;
        this.nickname = nickname;
        this.score = score;
        this.rank = rank;
    }

    public AuthenticatedUserDto(UUID clientUuid, String nickname, int score, int rank, String jwt) {
        this.clientUuid = clientUuid;
        this.nickname = nickname;
        this.score = score;
        this.rank = rank;
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public UUID getClientUuid() {
        return clientUuid;
    }

    public void setClientUuid(UUID clientUuid) {
        this.clientUuid = clientUuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean sameUser(UUID uuid){
        return this.clientUuid.equals(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AuthenticatedUserDto authenticatedUserDto = (AuthenticatedUserDto) o;

        return new EqualsBuilder().append(getScore(), authenticatedUserDto.getScore()).append(getRank(), authenticatedUserDto.getRank()).append(getClientUuid(), authenticatedUserDto.getClientUuid()).append(getNickname(), authenticatedUserDto.getNickname()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getClientUuid()).append(getNickname()).append(getScore()).append(getRank()).toHashCode();
    }
}
