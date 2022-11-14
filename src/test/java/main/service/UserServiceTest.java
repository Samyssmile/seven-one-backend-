package main.service;

import io.quarkus.test.junit.QuarkusTest;
import main.dto.AuthenticatedUserDto;
import main.entity.UserEntity;
import main.request.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class UserServiceTest {

    @Inject
    UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldRemoveUserByUuid() {
        AuthenticatedUserDto testAuthenticatedUserDto = userService.saveNewUser(getPredefinedCreateUserRequest());

        List<UserEntity> allUsers = userService.findAllUsers();
        Optional<UserEntity> subject = allUsers.stream().filter(userEntity -> userEntity.getClientUuid().equals(testAuthenticatedUserDto.getClientUuid())).findFirst();
        assertTrue(subject.isPresent());

        userService.removeUserByUuid(testAuthenticatedUserDto.getClientUuid());
        allUsers = userService.findAllUsers();
        subject = allUsers.stream().filter(userEntity -> userEntity.getClientUuid().equals(testAuthenticatedUserDto.getClientUuid())).findFirst();
        assertFalse(subject.isPresent());

    }

    private CreateUserRequest getPredefinedCreateUserRequest() {
        UUID clientUuis = UUID.randomUUID();
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setClientUuid(clientUuis);
        createUserRequest.setNickname("JonSnow");
        return createUserRequest;
    }

    @Test
    void shouldSaveNewUser() {
        AuthenticatedUserDto testAuthenticatedUserDto = userService.saveNewUser(getPredefinedCreateUserRequest());
        userService.findAllUsers().stream().filter(userEntity -> userEntity.getClientUuid().equals(testAuthenticatedUserDto.getClientUuid())).findFirst().ifPresent(userEntity -> {
            assertTrue(userEntity.getNickname().contains("#"));
        });
    }
}
