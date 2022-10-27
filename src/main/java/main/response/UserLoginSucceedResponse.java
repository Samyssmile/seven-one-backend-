package main.response;

import java.util.UUID;

public class UserLoginSucceedResponse {
    private UUID uuid;
    private UUID clientUuid;
    private String nickname;
    private int score;
    private int rank;
    private String jwt;

    public UserLoginSucceedResponse() {

    }

    public UserLoginSucceedResponse(UUID uuid, UUID clientUuid, String nickname, int score, int rank, String jwt) {
        this.uuid = uuid;
        this.clientUuid = clientUuid;
        this.nickname = nickname;
        this.score = score;
        this.rank = rank;
        this.jwt = jwt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "UserLoginSucceedResponse{" +
                "uuid=" + uuid +
                ", clientUuid=" + clientUuid +
                ", nickname='" + nickname + '\'' +
                ", score=" + score +
                ", rank=" + rank +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}
