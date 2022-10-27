package main.dto;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class PredictionDto {
    private UUID clientUuid;
    private UUID gameUuid;
    private String prediction;

    public PredictionDto() {
    }

    public PredictionDto(UUID clientUuid, UUID gameUuid, String prediction) {
        this.clientUuid = clientUuid;
        this.gameUuid = gameUuid;
        this.prediction = prediction;
    }

    public UUID getClientUuid() {
        return clientUuid;
    }

    public void setClientUuid(UUID clientUuid) {
        this.clientUuid = clientUuid;
    }

    public UUID getGameUuid() {
        return gameUuid;
    }

    public void setGameUuid(UUID gameUuid) {
        this.gameUuid = gameUuid;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PredictionDto that = (PredictionDto) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder().append(getClientUuid(), that.getClientUuid()).append(getGameUuid(), that.getGameUuid()).append(getPrediction(), that.getPrediction()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getClientUuid()).append(getGameUuid()).append(getPrediction()).toHashCode();
    }
}
