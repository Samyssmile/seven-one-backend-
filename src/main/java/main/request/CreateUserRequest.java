package main.request;

import java.util.UUID;

public class CreateUserRequest {
    private UUID clientUuid;
    private String nickname;

    private int score;


    /**
     * ModelMapper C'Tor
     */
    public CreateUserRequest() {
    }

    public CreateUserRequest(UUID clientUuid) {
        this.clientUuid = clientUuid;
    }

    public CreateUserRequest(UUID clientUuid, String nickname) {
        this.clientUuid = clientUuid;
        this.nickname = nickname;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UUID getClientUuid() {
        return clientUuid;
    }

    public void setClientUuid(UUID clientUuid) {
        this.clientUuid = clientUuid;
    }
}

