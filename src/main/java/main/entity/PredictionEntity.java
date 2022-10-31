package main.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "prediction")
public class PredictionEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @Column(name = "uuid", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;
    @Column
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID clientUuid;
    @Column
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID matchUuid;
    @Column
    private String prediction;

    public PredictionEntity() {
    }


    public PredictionEntity(UUID uuid, UUID clientUuid, UUID matchUuid, String prediction) {
        this.uuid = uuid;
        this.clientUuid = clientUuid;
        this.matchUuid = matchUuid;
        this.prediction = prediction;
    }

    public UUID getMatchUuid() {
        return matchUuid;
    }

    public void setMatchUuid(UUID matchUuid) {
        this.matchUuid = matchUuid;
    }

    public static List<PredictionEntity> findByClientUuid(UUID uuid) {
        return find("clientUuid", uuid).list();
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


    public void setClientUuid(UUID userUuid) {
        this.clientUuid = userUuid;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }
}
