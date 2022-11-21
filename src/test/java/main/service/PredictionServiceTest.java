package main.service;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import main.dto.AuthenticatedUserDto;
import main.dto.MatchDto;
import main.dto.PredictionDto;
import main.dto.TeamDto;
import main.entity.PredictionEntity;
import main.request.CreateUserRequest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class PredictionServiceTest {
    @InjectMock
    MatchService matchService;
    @Inject
    PredictionService predictionService;
    @Inject
    UserService userService;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();


    private Optional<MatchDto> generateMatchDto() {
        MatchDto matchDto = new MatchDto();
        matchDto.setUuid(UUID.randomUUID());
        matchDto.setFirstTeam(new TeamDto("team1", "team1"));
        matchDto.setSecondTeam(new TeamDto("team2", "team2"));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        matchDto.setMatchDate(calendar.getTime());
        return Optional.of(matchDto);
    }


    @Test
    void shouldBeSingleRecordOnMultiplaceSaveOperations() throws PredictionTimeExpiredException {
        when(matchService.getGameByUuid(any())).thenReturn(generateMatchDto());
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("testUser1");
        createUserRequest.setScore(0);
        createUserRequest.setClientUuid(UUID.randomUUID());

        AuthenticatedUserDto savedUser = userService.saveNewUser(createUserRequest, true);
        this.predictionService.clearPredictions();

        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setClientUuid(savedUser.getClientUuid());
        predictionDto.setMatchUuid(UUID.randomUUID());
        predictionDto.setPrediction("3:5");

        Optional<PredictionEntity> savedEntity = predictionService.makePrediction(predictionDto);
        predictionService.makePrediction(predictionDto);
        predictionService.makePrediction(predictionDto);
        predictionService.makePrediction(predictionDto);

        List<PredictionDto> persistedPredictions = predictionService.findAllPredictions();
        assertEquals(1, persistedPredictions.size());
        System.out.println(persistedPredictions.size());
    }

    @Test
    void shouldUpdatePrediction() throws PredictionTimeExpiredException {
        when(matchService.getGameByUuid(any())).thenReturn(generateMatchDto());
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("testUser1");
        createUserRequest.setScore(0);
        createUserRequest.setClientUuid(UUID.randomUUID());

        AuthenticatedUserDto savedUser = userService.saveNewUser(createUserRequest, false);

        this.predictionService.clearPredictions();
        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setClientUuid(savedUser.getClientUuid());
        predictionDto.setMatchUuid(UUID.randomUUID());
        predictionDto.setPrediction("3:5");

        Optional<PredictionEntity> savedEntity = predictionService.makePrediction(predictionDto);
        predictionService.makePrediction(predictionDto);
        predictionDto.setPrediction("5:5");
        predictionService.makePrediction(predictionDto);

        List<PredictionDto> persistedPredictions = predictionService.findAllPredictions();
        assertEquals(1, persistedPredictions.size());
        assertEquals("5:5", persistedPredictions.get(0).getPrediction());
        assertEquals(predictionDto.getMatchUuid(), persistedPredictions.get(0).getMatchUuid());
        assertEquals(predictionDto.getClientUuid(), persistedPredictions.get(0).getClientUuid());
        System.out.println(persistedPredictions.size());
    }

    @Test
    void shouldNotPredictOnExpiredGame() throws PredictionTimeExpiredException {
        when(matchService.getGameByUuid(any())).thenReturn(generateExpiredMatchDto());
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("testUser1");
        createUserRequest.setScore(0);
        createUserRequest.setClientUuid(UUID.randomUUID());

        AuthenticatedUserDto savedUser = userService.saveNewUser(createUserRequest, false);

        this.predictionService.clearPredictions();
        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setClientUuid(savedUser.getClientUuid());
        predictionDto.setMatchUuid(UUID.randomUUID());
        predictionDto.setPrediction("3:5");

        assertThrows(PredictionTimeExpiredException.class, () -> {
            predictionService.makePrediction(predictionDto);
        });
    }

    private Optional<MatchDto> generateExpiredMatchDto() {
        Optional<MatchDto> matchDto = this.generateMatchDto();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        matchDto.get().setMatchDate(calendar.getTime());
        return matchDto;
    }

    @Test
    void shouldFindAllPredictionsByClientUuid() {
        when(matchService.getGameByUuid(any())).thenReturn(generateMatchDto());
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("testUser1");
        createUserRequest.setScore(0);
        createUserRequest.setClientUuid(UUID.randomUUID());

        AuthenticatedUserDto savedUser = userService.saveNewUser(createUserRequest, false);
        this.predictionService.clearPredictions();
        int numberOfPredictions = 30;
        UUID clientUuid = savedUser.getClientUuid();
        List<PredictionDto> predictionList = getDummyPredictionsOfClient(clientUuid, numberOfPredictions);

        predictionList.forEach(predictionDto -> {
            try {
                predictionService.makePrediction(predictionDto);
            } catch (PredictionTimeExpiredException e) {
                throw new RuntimeException(e);
            }
        });

        List<PredictionDto> persistedPredictions = predictionService.findAllPredictionsByClientUuid(clientUuid);
        assertEquals(numberOfPredictions, persistedPredictions.size());
        persistedPredictions
                .forEach(predictionDto -> assertEquals(clientUuid, predictionDto.getClientUuid()));
        System.out.println(persistedPredictions.size());
    }

    private List<PredictionDto> getDummyPredictionsOfClient(UUID clientUuid, int i) {
        List<PredictionDto> resultList = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            PredictionDto predictionDto = new PredictionDto();
            predictionDto.setClientUuid(clientUuid);
            predictionDto.setMatchUuid(UUID.randomUUID());
            predictionDto.setPrediction(random.nextInt(0, 10) + ":" + random.nextInt(0, 10));
            resultList.add(predictionDto);
        }
        return resultList;
    }

    private String getRandomPrediction() {
        return random.nextInt(10) + ": " + random.nextInt(10);
    }
}
