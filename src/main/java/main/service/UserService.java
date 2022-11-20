package main.service;

import main.dto.AuthenticatedUserDto;
import main.dto.Role;
import main.dto.UserDto;
import main.entity.PredictionEntity;
import main.entity.UserEntity;
import main.request.CreateUserRequest;
import main.response.UserLoginSucceedResponse;
import main.security.JwtService;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.logging.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    //Logger
    private static final Logger logger = Logger.getLogger(UserService.class);
    private final JwtService jwtService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Inject
    public UserService(JwtService jwtService) {
        this.jwtService = jwtService;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Transactional
    public AuthenticatedUserDto saveNewUser(CreateUserRequest createUserRequest, boolean bot) {
        UserEntity userEntity = modelMapper.map(createUserRequest, UserEntity.class);
        userEntity.setUuid(null);
        userEntity.setRole(Role.USER);
        userEntity.setNickname(createUserRequest.getNickname() + "#" + generateFourDigitNumber());
        userEntity.setScore(createUserRequest.getScore());
        userEntity.setBot(bot);
        userEntity.persistAndFlush();
        AuthenticatedUserDto authenticatedUserDto = modelMapper.map(userEntity, AuthenticatedUserDto.class);
        authenticatedUserDto.setJwt(jwtService.generateUserJwt());
        return authenticatedUserDto;
    }

    private int generateFourDigitNumber() {
        return (int) (Math.random() * 9000) + 1000;
    }

    public List<UserEntity> findAllUsers() {
        return UserEntity.listAll();
    }

    public Optional<UserLoginSucceedResponse> findUserByClientUuid(UUID uuid) {

        UserEntity foundUserEntity = UserEntity.find("clientUuid", uuid).firstResult();
        Optional<UserLoginSucceedResponse> result = Optional.empty();
        if (foundUserEntity != null) {
            String jwt = jwtService.generateUserJwt();
            result = Optional.of(modelMapper.map(foundUserEntity, UserLoginSucceedResponse.class));
            result.get().setJwt(jwt);
        }
        return result;
    }

    public List<UserDto> getRankingList() {
        List<UserEntity> userEntities = UserEntity.findAll().list();
        userEntities.sort((o1, o2) -> o2.getScore() - o1.getScore());
        List<UserDto> rankedAuthenticatedUserDtoListDto = userEntities.stream().toList().stream().map(entity -> modelMapper.map(entity, UserDto.class)).toList();
        rankedAuthenticatedUserDtoListDto.forEach(userDto -> userDto.setRank(rankedAuthenticatedUserDtoListDto.indexOf(userDto) + 1));

        return rankedAuthenticatedUserDtoListDto;
    }

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<UserDto> getRankingListCached() {
        List<UserEntity> userEntities = UserEntity.findAll().list();
        userEntities.sort((o1, o2) -> o2.getScore() - o1.getScore());
        List<UserDto> rankedAuthenticatedUserDtoListDto = userEntities.stream().toList().stream().map(entity -> modelMapper.map(entity, UserDto.class)).toList();
        rankedAuthenticatedUserDtoListDto.forEach(userDto -> userDto.setRank(rankedAuthenticatedUserDtoListDto.indexOf(userDto) + 1));

        return rankedAuthenticatedUserDtoListDto;
    }

    public List<UserDto> getRankingListExperimental() {
        List<UserEntity> userEntities = UserEntity.findAll().list();
        userEntities.sort((o1, o2) -> o2.getScore() - o1.getScore());
        List<UserDto> rankedAuthenticatedUserDtoListDto = userEntities.stream().toList().stream().map(entity -> modelMapper.map(entity, UserDto.class)).toList();
        rankedAuthenticatedUserDtoListDto.forEach(userDto -> userDto.setRank(rankedAuthenticatedUserDtoListDto.indexOf(userDto) + 1));

        return rankedAuthenticatedUserDtoListDto;
    }

    @Transactional
    public boolean removeUserByUuid(UUID clientUuid) {
        UserEntity userEntity = UserEntity.find("clientUuid", clientUuid).firstResult();
        if (userEntity != null) {
            userEntity.delete("clientUuid", clientUuid);
            PredictionEntity.findByClientUuid(clientUuid).forEach(PredictionEntity::delete);
            return true;
        }
        return false;
    }
}
