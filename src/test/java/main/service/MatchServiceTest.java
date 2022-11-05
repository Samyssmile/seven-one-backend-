package main.service;

import io.quarkus.test.junit.QuarkusTest;
import main.dto.AuthenticatedUserDto;
import main.dto.MatchDto;
import main.dto.PredictionDto;
import main.entity.PredictionEntity;
import main.request.CreateUserRequest;
import main.request.MatchUpdateRequest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class MatchServiceTest {
    private static final Logger LOGGER = Logger.getLogger("MatchServiceTest");
    @Inject
    MatchService matchService;
    @Inject
    UserService userService;

    @Inject
    PredictionService predictionService;

    @Test
    void updateGame() {
        List<MatchDto> allMatches = matchService.getAllMatches();
        MatchDto match = allMatches.get(0);

        Optional<MatchDto> updatedMatch = matchService.updateMatchResult(new MatchUpdateRequest(match.getUuid(), "9:9"));

        assertTrue(updatedMatch.isPresent());
        assertTrue(updatedMatch.get().getResult().equals("9:9"));

        LOGGER.info(allMatches);
    }

    @Test
    void shouldUserScoresMaxScore() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("testUser1");
        createUserRequest.setScore(0);
        createUserRequest.setClientUuid(UUID.randomUUID());


        List<MatchDto> allMatches = matchService.getAllMatches();
        MatchDto match = allMatches.get(1);

        AuthenticatedUserDto savedUser = userService.saveNewUser(createUserRequest);
        this.predictionService.clearPredictions();
        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setClientUuid(savedUser.getClientUuid());
        predictionDto.setMatchUuid(match.getUuid());
        predictionDto.setPrediction("3:5");

        Optional<PredictionEntity> predictionOfTestUser = predictionService.makePrediction(predictionDto);
        Optional<MatchDto> updatedMatch = matchService.updateMatchResult(new MatchUpdateRequest(match.getUuid(), "3:5"));

        userService.findUserByClientUuid(savedUser.getClientUuid()).ifPresentOrElse(user -> {
            assertTrue(user.getScore() == 3);
        }, () -> {
            assertTrue(false);
        });

        LOGGER.info(allMatches);
    }

    @Test
    void shouldUserScoresPartialScore() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("testUser2");
        createUserRequest.setScore(0);
        createUserRequest.setClientUuid(UUID.randomUUID());


        List<MatchDto> allMatches = matchService.getAllMatches();
        MatchDto match = allMatches.get(1);

        AuthenticatedUserDto savedUser = userService.saveNewUser(createUserRequest);
        this.predictionService.clearPredictions();
        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setClientUuid(savedUser.getClientUuid());
        predictionDto.setMatchUuid(match.getUuid());
        predictionDto.setPrediction("3:5");

        Optional<PredictionEntity> predictionOfTestUser = predictionService.makePrediction(predictionDto);
        Optional<MatchDto> updatedMatch = matchService.updateMatchResult(new MatchUpdateRequest(match.getUuid(), "3:6"));

        userService.findUserByClientUuid(savedUser.getClientUuid()).ifPresentOrElse(user -> {
            assertTrue(user.getScore() == 2);
        }, () -> {
            assertTrue(false);
        });

        LOGGER.info(allMatches);
    }

    @Test
    void shouldUserScoresMinimalScore() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("testUser3");
        createUserRequest.setScore(0);
        createUserRequest.setClientUuid(UUID.randomUUID());


        List<MatchDto> allMatches = matchService.getAllMatches();
        MatchDto match = allMatches.get(1);

        AuthenticatedUserDto savedUser = userService.saveNewUser(createUserRequest);
        this.predictionService.clearPredictions();
        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setClientUuid(savedUser.getClientUuid());
        predictionDto.setMatchUuid(match.getUuid());
        predictionDto.setPrediction("3:5");

        Optional<PredictionEntity> predictionOfTestUser = predictionService.makePrediction(predictionDto);
        Optional<MatchDto> updatedMatch = matchService.updateMatchResult(new MatchUpdateRequest(match.getUuid(), "2:8"));

        userService.findUserByClientUuid(savedUser.getClientUuid()).ifPresentOrElse(user -> {
            assertTrue(user.getScore() == 1);
        }, () -> {
            assertTrue(false);
        });

        LOGGER.info(allMatches);
    }

    @Test
    void shouldUserScoresNothing() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("testUser4");
        createUserRequest.setScore(0);
        createUserRequest.setClientUuid(UUID.randomUUID());


        List<MatchDto> allMatches = matchService.getAllMatches();
        MatchDto match = allMatches.get(1);

        AuthenticatedUserDto savedUser = userService.saveNewUser(createUserRequest);
        this.predictionService.clearPredictions();
        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setClientUuid(savedUser.getClientUuid());
        predictionDto.setMatchUuid(match.getUuid());
        predictionDto.setPrediction("3:5");

        Optional<PredictionEntity> predictionOfTestUser = predictionService.makePrediction(predictionDto);
        Optional<MatchDto> updatedMatch = matchService.updateMatchResult(new MatchUpdateRequest(match.getUuid(), "5:3"));

        userService.findUserByClientUuid(savedUser.getClientUuid()).ifPresentOrElse(user -> {
            assertTrue(user.getScore() == 0);
        }, () -> {
            assertTrue(false);
        });

        LOGGER.info(allMatches);
    }
}
