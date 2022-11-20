package main.service;

import io.quarkus.test.junit.QuarkusTest;
import main.dto.AuthenticatedUserDto;
import main.dto.PredictionDto;
import main.entity.PredictionEntity;
import main.request.CreateUserRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class PredictionServiceTest {
    private final PredictionService predictionService;
    private final UserService userService;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public PredictionServiceTest(PredictionService predictionService, UserService userService) {
        this.predictionService = predictionService;
        this.userService = userService;
    }

    @Test
    void shouldBeSingleRecordOnMultiplaceSaveOperations() {
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
    void shouldUpdatePrediction() {
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
    void shouldFindAllPredictionsByClientUuid() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("testUser1");
        createUserRequest.setScore(0);
        createUserRequest.setClientUuid(UUID.randomUUID());

        AuthenticatedUserDto savedUser = userService.saveNewUser(createUserRequest, false);
        this.predictionService.clearPredictions();
        int numberOfPredictions = 30;
        UUID clientUuid = savedUser.getClientUuid();
        List<PredictionDto> predictionList = getDummyPredictionsOfClient(clientUuid, numberOfPredictions);

        predictionList.forEach(predictionDto -> predictionService.makePrediction(predictionDto));

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
