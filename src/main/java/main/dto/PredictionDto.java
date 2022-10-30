package main.dto;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class PredictionDto {
    private UUID clientUuid;
    private UUID matchUuid;
    private String prediction;

    public PredictionDto() {
    }

    public PredictionDto(UUID clientUuid, UUID matchUuid, String prediction) {
        this.clientUuid = clientUuid;
        this.matchUuid = matchUuid;
        this.prediction = prediction;
    }

    public UUID getClientUuid() {
        return clientUuid;
    }

    public void setClientUuid(UUID clientUuid) {
        this.clientUuid = clientUuid;
    }

    public UUID getMatchUuid() {
        return matchUuid;
    }

    public void setMatchUuid(UUID matchUuid) {
        this.matchUuid = matchUuid;
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

        return new org.apache.commons.lang3.builder.EqualsBuilder().append(getClientUuid(), that.getClientUuid()).append(getMatchUuid(), that.getMatchUuid()).append(getPrediction(), that.getPrediction()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getClientUuid()).append(getMatchUuid()).append(getPrediction()).toHashCode();
    }
}
