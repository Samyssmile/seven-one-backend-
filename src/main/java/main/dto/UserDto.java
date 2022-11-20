package main.dto;

import java.util.UUID;

public class UserDto {
    private UUID uuid;
    private UUID clientUuid;
    private String nickname;
    private int score;
    private int rank;
    private boolean bot;

    public UserDto() {
    }

    public UserDto(UUID uuid, UUID clientUuid, String nickname, int score, int rank, boolean bot) {
        this.uuid = uuid;
        this.clientUuid = clientUuid;
        this.nickname = nickname;
        this.score = score;
        this.rank = rank;
        this.bot = bot;
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

    public boolean sameUser(UUID uuid) {
        return this.clientUuid.equals(uuid);
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

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }
}
