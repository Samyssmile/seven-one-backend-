package main.service.migration;

import io.quarkus.test.junit.QuarkusTest;
import main.service.PredictionService;
import main.service.UserService;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class ProdDataMigrationTest {
    @Inject
    UserService userService;
    @Inject
    PredictionService predictionService;
    @Inject
    DataPersistance dataPersistance;

    @Test
    void migrate() {
        ProdDataMigration prodDataMigration = new ProdDataMigration(userService, predictionService, dataPersistance);
        prodDataMigration.migrate();
    }
}
