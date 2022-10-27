package main.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import main.dto.GameDto;
import main.dto.PredictionDto;
import main.dto.Result;
import main.entity.GameEntity;
import main.entity.PredictionEntity;
import main.request.CreateGameRequest;
import main.request.GameUpdateRequest;
import org.jboss.logging.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameService {
    private static final Logger LOGGER = Logger.getLogger("GameService");
    private final PredictionService predictionService;
    private ModelMapper modelMapper = new ModelMapper();

    @Inject
    public GameService(PredictionService predictionService) {
        this.predictionService = predictionService;
        //strict matchingstrategie strict
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

//    public List<GameByUserResponse> findGamesByUserUuid(UUID uuid) {
//        List<PredictionEntity> predictionEntity = PredictionEntity.findByUserUuid(uuid);
//        List<GameEntity> gameEntity = GameEntity.listAll();
//        List<GameByUserResponse> gameByUserResponses = new ArrayList<>();
//        gameEntity.forEach(game -> {
//            predictionEntity.forEach(prediction -> {
//                if (game.getUuid().equals(prediction.getGameUuid())) {
//                    GameByUserResponse gameByUserResponse = new GameByUserResponse();
//                    gameByUserResponse.setGroup(game.getGroupName());
//                    gameByUserResponse.setFirstTeam(game.getFirstTeam());
//                    gameByUserResponse.setSecondTeam(game.getSecondTeam());
//                    gameByUserResponse.setMatchDate(game.getMatchDate());
//                    gameByUserResponse.setResult(game.getResult());
//                    gameByUserResponse.setPredictionDto(modelMapper.map(prediction, PredictionDto.class));
//                    gameByUserResponses.add(gameByUserResponse);
//                }
//            });
//        });
//
//        return gameByUserResponses;
//    }

    @Transactional
    public Optional<GameEntity> saveNewGame(CreateGameRequest createGameRequest) {
        var gameEntity = modelMapper.map(createGameRequest, GameEntity.class);
        gameEntity.persistAndFlush();
        return Optional.of(gameEntity);
    }

    public List<GameDto> getAllGames() {
        PanacheQuery<GameEntity> gameEntityList = GameEntity.findAll();
        List<GameDto> gameDtoList = gameEntityList.stream().map(gameEntity -> modelMapper.map(gameEntity, GameDto.class)).collect(Collectors.toList());
        return gameDtoList;
    }

    public Optional<GameDto> getGameByUuid(UUID uuid) {
        GameEntity gameEntity = GameEntity.find("uuid", uuid).firstResult();
        GameDto result = modelMapper.map(gameEntity, GameDto.class);
        return Optional.ofNullable(result);
    }

    public List<GameDto> getUnpredictedGamesByUser(UUID clientUuid) {
        List<PredictionEntity> predictionEntityList = PredictionEntity.findByClientUuid(clientUuid);
        List<GameEntity> gameEntityList = GameEntity.listAll();
        predictionEntityList.forEach(predictionEntity -> {
            gameEntityList.removeIf(gameEntity -> gameEntity.getUuid().equals(predictionEntity.getGameUuid()));
        });
        List<GameDto> gameDtoList = gameEntityList.stream().map(gameEntity -> modelMapper.map(gameEntity, GameDto.class)).collect(Collectors.toList());
        return gameDtoList;
    }

    public Optional<GameDto> updateGame(GameUpdateRequest gameDto) {
        String result = gameDto.getResult();
        if (result != null) {
            this.updateAndTriggerScoreUpdate(gameDto);
        } else {
            this.updateGameWithOutScoreUpdate(gameDto);
        }

        return null;
    }

    private void updateGameWithOutScoreUpdate(GameUpdateRequest gameDto) {
        GameEntity gameEntity = modelMapper.map(gameDto, GameEntity.class);
        gameEntity.persistAndFlush();
    }


    private void updateAndTriggerScoreUpdate(GameUpdateRequest gameDto) {
        List<PredictionDto> predictionDtos = this.predictionService.findAllPredictionsByGameUuid(gameDto.getUuid());

        predictionDtos.forEach(predictionDto -> {
            int scoreEarned = getScoreForPrediction(predictionDto, gameDto);
            if(scoreEarned > 0){
                this.predictionService.updateScoreForPrediction(predictionDto.getClientUuid(), scoreEarned);
            }
        });
        // If Score matched, update Users score

    }

    private int getScoreForPrediction(PredictionDto predictionDto, GameUpdateRequest gameDto) {
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

    public Set<GameDto> getPredictedGamesByUser(UUID clientUuid) {
        List<PredictionEntity> predictionEntityList = PredictionEntity.findByClientUuid(clientUuid);
        Set<GameDto> gameDtoSet = new HashSet<>();
        predictionEntityList.forEach(predictionEntity -> {
            GameEntity gameEntity = GameEntity.find("uuid", predictionEntity.getGameUuid()).firstResult();
            GameDto gameDto = modelMapper.map(gameEntity, GameDto.class);
            gameDtoSet.add(gameDto);
        });
        return gameDtoSet;
    }
}
