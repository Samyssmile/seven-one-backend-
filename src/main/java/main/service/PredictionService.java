package main.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.security.UnauthorizedException;
import main.dto.PredictionDto;
import main.entity.PredictionEntity;
import main.entity.UserEntity;
import org.jboss.logging.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class PredictionService {
    private static final Logger logger = Logger.getLogger(PredictionService.class);
    //Pattern for 3:5
    private static final Pattern pattern = Pattern.compile("^(\\d+):(\\d+)$");

    private ModelMapper modelMapper = new ModelMapper();

    public PredictionService() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Transactional
    public Optional<PredictionEntity> makePrediction(PredictionDto predictionDto) {
        boolean valid = validatePrediction(predictionDto.getPrediction());
        boolean checkUserExists = UserEntity.findByIdOptional(predictionDto.getClientUuid()).isPresent();

        if (!checkUserExists){
            throw new UnauthorizedException();
        }
        if (valid ){
            PredictionEntity entity = modelMapper.map(predictionDto, PredictionEntity.class);
            List<PredictionEntity> clientPredictions = PredictionEntity.find("clientUuid", predictionDto.getClientUuid()).list();
            List<PredictionEntity> persistedIdenticalEntities = clientPredictions.stream().filter(p->p.getGameUuid().equals(predictionDto.getGameUuid())).toList();
            if (persistedIdenticalEntities.size() == 0) {
                entity.persistAndFlush();
            }else{
                persistedIdenticalEntities.get(0).setPrediction(predictionDto.getPrediction());
                persistedIdenticalEntities.get(0).persistAndFlush();
            }

            // Patter match number - number
            Matcher matcher = pattern.matcher(entity.getPrediction());
            if(matcher.matches()) {
                logger.info("Prediction made: " + entity.getPrediction() + "; Game " + entity.getGameUuid() + "; Firstteam " + entity.getPrediction().split(":")[0] + "; Secondteam" + entity.getPrediction().split(":")[1]);
                return Optional.ofNullable(entity);
            } else {
                logger.info("Prediction Pattern not matches: " + entity.getPrediction());
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private boolean validatePrediction(String prediction) {
        String[] preditedResults = prediction.split(":");
        if (preditedResults.length != 2) {
            return false;
        }
        try {
            int firstTeam = Integer.parseInt(preditedResults[0]);
            int secondTeam = Integer.parseInt(preditedResults[1]);
            if (firstTeam < 0 || secondTeam < 0) {
                return false;
            }

            if (firstTeam>10 || secondTeam>10) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        if (prediction.length() > 5) {
            return false;
        }
        return true;
    }

    public List<PredictionDto> findAll() {
        List<PredictionDto> resultDtoList = PredictionEntity.findAll().stream().map(entity -> modelMapper.map(entity, PredictionDto.class)).toList();
        return resultDtoList;
    }

    public List<PredictionDto> findAllPredictionsByClientUuid(UUID clientUuid) {
        List<PredictionEntity> filteredList = PredictionEntity.find("clientUuid", clientUuid).list();
        List<PredictionDto> resultDtoList = filteredList.stream().map(entity -> modelMapper.map(entity, PredictionDto.class)).toList();
        return resultDtoList;
    }

    public List<PredictionDto> findAllPredictions() {
        PanacheQuery<PredictionEntity> entityList = PredictionEntity.findAll();
        List<PredictionDto> resultDtoList = entityList.stream().map(entity -> modelMapper.map(entity, PredictionDto.class)).toList();
        return resultDtoList;
    }

    public List<PredictionDto> findAllPredictionsByGameUuid(UUID gameUuid) {
        List<PredictionEntity> filteredList = PredictionEntity.find("gameUuid", gameUuid).list();

        return filteredList.stream().map(entity -> modelMapper.map(entity, PredictionDto.class)).toList();
    }

    @Transactional
    public void updateScoreForPrediction(UUID clientUuid, int scoreEarned) {
        PanacheQuery<UserEntity> userToUpdate = UserEntity.find("clientUuid", clientUuid);
        int size = userToUpdate.list().size();
        Optional<UserEntity> optionalSubject = userToUpdate.stream().findAny();
        if (optionalSubject.isEmpty()) {
            System.out.println("No user found");
        }
        int oldScore = userToUpdate.firstResult().getScore();
        oldScore += scoreEarned;
        userToUpdate.firstResult().setScore(oldScore);
        userToUpdate.firstResult().persistAndFlush();
    }

    @Transactional
    public void clearPredictions() {
        PredictionEntity.deleteAll();
    }
}
