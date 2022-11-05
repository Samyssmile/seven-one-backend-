package main.service.migration;

import main.entity.MatchEntity;
import main.entity.UserEntity;

import java.util.List;
import java.util.Random;

public interface IDataMigration {

    void migrate();

    void migrateTeams();

    void migrateGroups();

    void migrateMatchSchedule();

    void migrateUsers(int amountOfUsers);

    void migratePredictions(List<UserEntity> userEntityList, List<MatchEntity> matchEntityList);


    public default String getRandomPredictionScore() {
        int homeScore = (int) (Math.random() * 10);
        int awayScore = (int) (Math.random() * 10);
        return homeScore + ":" + awayScore;
    }

    public default int generateRandomScore() {
        return new Random().ints(0, 100).findFirst().getAsInt();
    }

    public default long getRandomMinutes() {
        return (long) (Math.random() * 60);
    }

    public default long getRandomHours() {
        return (long) (Math.random() * 24);
    }
}
