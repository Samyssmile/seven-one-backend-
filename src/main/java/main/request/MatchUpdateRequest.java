package main.request;

import java.util.UUID;

public class MatchUpdateRequest {
    private UUID matchUuid;
    private String result;

    public MatchUpdateRequest() {
    }

    public MatchUpdateRequest(UUID uuid, String result) {
        this.matchUuid = uuid;
        this.result = result;
    }

    public UUID getMatchUuid() {
        return matchUuid;
    }

    public void setMatchUuid(UUID matchUuid) {
        this.matchUuid = matchUuid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
