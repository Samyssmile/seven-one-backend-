package main.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import main.dto.MatchDto;
import main.dto.PredictionDto;
import main.dto.Result;
import main.entity.MatchEntity;
import main.entity.PredictionEntity;
import main.request.CreateMatchRequest;
import main.request.MatchUpdateRequest;
import org.jboss.logging.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class MatchService {
    private static final Logger LOGGER = Logger.getLogger("GameService");
    private final PredictionService predictionService;
    private ModelMapper modelMapper = new ModelMapper();

    @Inject
    public MatchService(PredictionService predictionService) {
        this.predictionService = predictionService;
        //strict matchingstrategie strict
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Transactional
    public Optional<MatchEntity> saveNewGame(CreateMatchRequest createMatchRequest) {
        var gameEntity = modelMapper.map(createMatchRequest, MatchEntity.class);
        gameEntity.persistAndFlush();
        return Optional.of(gameEntity);
    }

    public List<MatchDto> getAllMatches() {
        PanacheQuery<MatchEntity> gameEntityList = MatchEntity.findAll();
        List<MatchDto> matchDtoList = gameEntityList.stream().map(matchEntity -> modelMapper.map(matchEntity, MatchDto.class)).collect(Collectors.toList());
        return matchDtoList;
    }

    public Optional<MatchDto> getGameByUuid(UUID uuid) {
        MatchEntity matchEntity = MatchEntity.find("uuid", uuid).firstResult();
        MatchDto result = modelMapper.map(matchEntity, MatchDto.class);
        return Optional.ofNullable(result);
    }

    public List<MatchDto> getUnpredictedMatchesByUser(UUID clientUuid) {
        List<PredictionEntity> predictionEntityList = PredictionEntity.findByClientUuid(clientUuid);
        List<MatchEntity> matchEntityList = MatchEntity.listAll();
        predictionEntityList.forEach(predictionEntity -> {
            matchEntityList.removeIf(matchEntity -> matchEntity.getUuid().equals(predictionEntity.getMatchUuid()));
        });
        List<MatchDto> matchDtoList = matchEntityList.stream().map(matchEntity -> modelMapper.map(matchEntity, MatchDto.class)).collect(Collectors.toList());
        return matchDtoList;
    }

    public Optional<MatchDto> updateGame(MatchUpdateRequest gameDto) {
        String result = gameDto.getResult();
        if (result != null) {
            this.updateAndTriggerScoreUpdate(gameDto);
        } else {
            this.updateGameWithOutScoreUpdate(gameDto);
        }

        return null;
    }

    private void updateGameWithOutScoreUpdate(MatchUpdateRequest gameDto) {
        MatchEntity matchEntity = modelMapper.map(gameDto, MatchEntity.class);
        matchEntity.persistAndFlush();
    }


    private void updateAndTriggerScoreUpdate(MatchUpdateRequest gameDto) {
        List<PredictionDto> predictionDtos = this.predictionService.findAllPredictionsByMatchUuid(gameDto.getUuid());

        predictionDtos.forEach(predictionDto -> {
            int scoreEarned = getScoreForPrediction(predictionDto, gameDto);
            if(scoreEarned > 0){
                this.predictionService.updateScoreForPrediction(predictionDto.getClientUuid(), scoreEarned);
            }
        });
    }

    private int getScoreForPrediction(PredictionDto predictionDto, MatchUpdateRequest gameDto) {
        int score = 0;
        String result = gameDto.getResult();
        Result endResult = getResultFromFirstTeamPerspective(result);
        Result predictedResult = getResultFromFirstTeamPerspective(predictionDto.getPrediction());
        if (endResult == predictedResult) {
            score +=1;
            score += calculateExactPredictedScore(result, predictionDto.getPrediction());
        }
        return score;
    }

    private int calculateExactPredictedScore(String result, String prediction) {
        int score = 0;
        int firstTeamScore = Integer.parseInt(result.split(":")[0]);
        int secondTeamScore = Integer.parseInt(result.split(":")[1]);

        int firstTeamScorePrediction = Integer.parseInt(prediction.split(":")[0]);
        int secondTeamScorePrediction = Integer.parseInt(prediction.split(":")[1]);
        if (firstTeamScore == firstTeamScorePrediction) {
            score += 1;
        }
        if (secondTeamScore == secondTeamScorePrediction) {
            score += 1;
        }
        return score;
    }

    private Result getResultFromFirstTeamPerspective(String result) {
        int firstTeamScore = Integer.parseInt(result.split(":")[0]);
        int secondTeamScore = Integer.parseInt(result.split(":")[1]);

        if (firstTeamScore > secondTeamScore) {
            return Result.WIN;
        }
        if (firstTeamScore < secondTeamScore) {
            return Result.LOSE;
        }
        if (firstTeamScore == secondTeamScore) {
            return Result.DRAW;
        }
        return Result.ABORTED;
    }

    public Set<MatchDto> getPredictedMatchesByUser(UUID clientUuid) {
        List<PredictionEntity> predictionEntityList = PredictionEntity.findByClientUuid(clientUuid);
        Set<MatchDto> matchDtoSet = new HashSet<>();
        predictionEntityList.forEach(predictionEntity -> {
            MatchEntity matchEntity = MatchEntity.find("uuid", predictionEntity.getMatchUuid()).firstResult();
            MatchDto matchDto = modelMapper.map(matchEntity, MatchDto.class);
            matchDtoSet.add(matchDto);
        });
        return matchDtoSet;
    }
}
