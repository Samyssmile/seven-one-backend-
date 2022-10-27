package main;

import io.quarkus.test.junit.QuarkusTest;
import main.dto.AuthenticatedUserDto;
import main.dto.GameDto;
import main.dto.PredictionDto;
import main.dto.UserDto;
import main.request.CreateGroupRequest;
import main.request.CreateUserRequest;
import main.request.GameUpdateRequest;
import main.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class GameResourceTest {

    private static UUID firstUserUUID;
    private static UUID secondUserUUID;
    private static UUID thirdUserUUID;

    @Inject
    GameResource gameResource;
    @Inject
    GameService gameService;
    @Inject
    UserService userService;
    @Inject
    PredictionService predictionService;
    @Inject
    GroupService groupService;
    @Inject
    TeamService teamService;
    // will be started before and stopped after each test method
    @BeforeAll
    public static void init() {
        firstUserUUID = UUID.randomUUID();
        secondUserUUID = UUID.randomUUID();
        thirdUserUUID = UUID.randomUUID();
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldLeadTheRankLadder() {
        // Given two games
        List<GameDto> allGames = gameService.getAllGames();
        GameDto firstGame = gameService.getAllGames().get(5);
        GameDto secondGame = gameService.getAllGames().get(6);

        // Create a new User JonSnow
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setClientUuid(firstUserUUID);
        createUserRequest.setNickname("JonSnow");
        AuthenticatedUserDto testAuthenticatedUserDto = userService.saveNewUser(createUserRequest);

        PredictionDto exactPrediction = new PredictionDto();
        exactPrediction.setPrediction("9:1");
        exactPrediction.setClientUuid(testAuthenticatedUserDto.getClientUuid());
        exactPrediction.setGameUuid(firstGame.getUuid());
        predictionService.makePrediction(exactPrediction);

        PredictionDto partialPrediction = new PredictionDto();
        partialPrediction.setPrediction("9:3");
        partialPrediction.setClientUuid(testAuthenticatedUserDto.getClientUuid());
        partialPrediction.setGameUuid(secondGame.getUuid());
        predictionService.makePrediction(partialPrediction);

        GameUpdateRequest firstGameUpdate = new GameUpdateRequest();
        firstGameUpdate.setResult("9:1");
        firstGameUpdate.setUuid(firstGame.getUuid());
        firstGameUpdate.setFirstTeam(firstGame.getFirstTeam());
        firstGameUpdate.setSecondTeam(firstGame.getSecondTeam());
        firstGameUpdate.setMatchDate(firstGame.getMatchDate());
        firstGameUpdate.setGroupName(firstGame.getGroupName());

        GameUpdateRequest secondGameUpdate = new GameUpdateRequest();
        secondGameUpdate.setResult("8:3");
        secondGameUpdate.setUuid(secondGame.getUuid());
        secondGameUpdate.setFirstTeam(secondGame.getFirstTeam());
        secondGameUpdate.setSecondTeam(secondGame.getSecondTeam());
        secondGameUpdate.setMatchDate(secondGame.getMatchDate());
        secondGameUpdate.setGroupName(secondGame.getGroupName());

        gameService.updateGame(firstGameUpdate);
        gameService.updateGame(secondGameUpdate);

        List<UserDto> rankedList = userService.getRankingList();
        assertTrue(rankedList.get(0).sameUser(testAuthenticatedUserDto.getClientUuid()));
        assertTrue(userService.findUserByClientUuid(testAuthenticatedUserDto.getClientUuid()).get().getScore() == 5);
        assertTrue(rankedList.get(0).getScore() == 5);

    }

    private CreateGroupRequest generateRandomGroup() {
        return new CreateGroupRequest("Z");

    }
}
