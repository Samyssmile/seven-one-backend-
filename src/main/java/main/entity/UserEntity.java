package main.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import main.dto.Role;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class UserEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue
    @Column(name = "uuid", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;

    @Column(name = "clientUuid")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID clientUuid;
    @Column
    private String nickname;
    @Column
    private int score;
    @Column
    private int rank;
    @Column
    private boolean bot;
    @Column
    private Role role;

    public UserEntity() {
    }

    public UserEntity(UUID uuid, UUID clientUuid, String nickname, int score, int rank, boolean bot, Role role) {
        this.uuid = uuid;
        this.clientUuid = clientUuid;
        this.nickname = nickname;
        this.score = score;
        this.rank = rank;
        this.bot = bot;
        this.role = role;
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

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
