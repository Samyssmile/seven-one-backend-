package main.service.migration;

import main.entity.GroupEntity;
import main.entity.MatchEntity;
import main.entity.PredictionEntity;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class DataPersistance {
    private static final Logger LOGGER = Logger.getLogger(DataPersistance.class);

    @Transactional
    public void persistGroupList(List<GroupEntity> groupEntityList) {
        GroupEntity.persist(groupEntityList);
        GroupEntity.flush();
        LOGGER.info("Persisted " + groupEntityList.size() + " groups");

    }

    @Transactional
    public void persistPredictions(List<PredictionEntity> predictionDtos) {
        PredictionEntity.persist(predictionDtos);
        PredictionEntity.flush();
    }

    @Transactional
    public void persistMatches(List<MatchEntity> matchEntities) {
        MatchEntity.persist(matchEntities);
        MatchEntity.flush();
        LOGGER.info("Persisted " + matchEntities.size() + " matches");
    }
}
