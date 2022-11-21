package main.service;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import main.service.migration.DataPersistance;
import main.service.migration.IDataMigration;
import main.service.migration.ProdDataMigration;
import main.service.migration.TestDataMigration;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class MigrationService {
    private static final Logger LOGGER = Logger.getLogger(MigrationService.class);
    private IDataMigration dataMigration;

    @Inject
    public MigrationService(UserService userService, PredictionService predictionService, DataPersistance dataPersistance) {
        String activeProfile = ProfileManager.getActiveProfile();
        if (activeProfile.equals("dev") || activeProfile.equals("test")) {
            dataMigration = new TestDataMigration(userService, predictionService, dataPersistance);
        } else if (activeProfile.equals("staging") || activeProfile.equals("prod"))
            dataMigration = new ProdDataMigration(userService, predictionService, dataPersistance);
    }


    void onStart(@Observes StartupEvent ev) {
        String activeProfile = ProfileManager.getActiveProfile();
        if (activeProfile.equals("dev") || activeProfile.equals("test") || activeProfile.equals("prod")) {
            LOGGER.info("Start migration");
            dataMigration.migrate();
            LOGGER.info("Migration finished");
        } else {
            LOGGER.info("Migration skipped");
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
