package main.service;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import main.dto.PredictionDto;
import main.entity.*;
import main.request.CreateUserRequest;
import main.utility.NameGenerator;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

@ApplicationScoped
public class AppLifecycleService {
    private static final Logger LOGGER = Logger.getLogger(AppLifecycleService.class);
    private final UserService userService;
    private final PredictionService predictionService;

    @Inject
    public AppLifecycleService(UserService userService, PredictionService predictionService) {
        this.predictionService = predictionService;
        this.userService = userService;
    }

    @Transactional
    public static void persistGroup(GroupEntity group) {
        group.persistAndFlush();
    }

    @Transactional
    public static void persistGame(MatchEntity matchEntity) {
        matchEntity.persistAndFlush();
    }

    private static String getRandomPredictionScore() {
        int homeScore = (int) (Math.random() * 10);
        int awayScore = (int) (Math.random() * 10);
        return homeScore + ":" + awayScore;
    }

    void onStart(@Observes StartupEvent ev) {
        String activeProfile = ProfileManager.getActiveProfile();
        LOGGER.info("The application is starting with profile " + activeProfile);
        if (activeProfile.equals("dev") || activeProfile.equals("test")) {
            List<TeamEntity> teamList = initTeams();
            List<GroupEntity> groupList = initGroups(teamList);
            List<MatchEntity> matchEntityList = initFifaWorld2022MatchSchedule(groupList);
            List<UserEntity> userEntityList = initUsers();
            initPredictions(userEntityList, matchEntityList);
        } else {
            LOGGER.info("No data initialization for profile " + activeProfile);
        }


        LOGGER.info("WM2022-App is starting...");
    }

    public String getValue() {
        Optional<String> value = Optional.of("Hello");
        // if value present, return value, else return IllegalArgumentException
        return value.orElseThrow(IllegalArgumentException::new);
    }

    private List<PredictionDto> initPredictions(List<UserEntity> userEntityList, List<MatchEntity> matchEntityList) {
        LOGGER.info("Init Predictions");
        userEntityList.parallelStream().forEach(userEntity -> {
            List<PredictionEntity> predictionDtos = new ArrayList<>();
            matchEntityList.forEach(matchEntity -> {
                PredictionEntity predictionEntity = new PredictionEntity();
                predictionEntity.setMatchUuid(matchEntity.getUuid());
                predictionEntity.setClientUuid(userEntity.getClientUuid());
                predictionEntity.setPrediction(getRandomPredictionScore());
                predictionDtos.add(predictionEntity);
            });
            persistPredictions(predictionDtos);
        });

        LOGGER.info("Predictions by users done...");
        return this.predictionService.findAll();
    }

    @Transactional
    public void persistPredictions(List<PredictionEntity> predictionDtos) {
        predictionDtos.forEach(element -> LOGGER.info("Testdata Generation: Persist Predictions " + element.getPrediction() + " Game: " + element.getMatchUuid()));
        PredictionEntity.persist(predictionDtos);
        PredictionEntity.flush();
    }

    private List<UserEntity> initUsers() {
        LOGGER.info("Init Users...");
        IntStream.range(0, 0).forEach(i -> {
            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setClientUuid(UUID.randomUUID());
            createUserRequest.setNickname(NameGenerator.generateName());
            createUserRequest.setScore(generateRandomScore());
            userService.saveNewUser(createUserRequest);
        });

        LOGGER.info("Users initialized");
        return userService.findAllUsers();
    }


    private int generateRandomScore() {
        return new Random().ints(0, 100).findFirst().getAsInt();
    }

    private List<MatchEntity> initFifaWorld2022MatchSchedule(List<GroupEntity> groupList) {
        List<MatchEntity> matchEntityList = new ArrayList<>();
        groupList.forEach(groupEntity -> {
            List<TeamEntity> teamEntityList = groupEntity.getTeams().stream().toList();
            for (int i = 0; i < teamEntityList.size(); i++) {
                for (int j = i + 1; j < teamEntityList.size(); j++) {
                    MatchEntity matchEntity = new MatchEntity();
                    matchEntity.setFirstTeam(teamEntityList.get(i));
                    matchEntity.setSecondTeam(teamEntityList.get(j));
                    matchEntity.setGroupName(groupEntity.getGroupName());
                    Date date;

                    if (Math.random() > 0.5) {
                        date = Date.from(LocalDate.now().plusDays(1 + i).atStartOfDay(ZoneId.systemDefault()).plusHours(getRandomHours()).plusMinutes(getRandomMinutes()).toInstant());
                    } else {
                        if (Math.random() > 0.5) {
                            //date instant now
                            date = Date.from(Instant.now());
                        } else {
                            date = Date.from(LocalDate.now().minusDays(1 + i).atStartOfDay(ZoneId.systemDefault()).plusHours(getRandomHours()).plusMinutes(getRandomMinutes()).toInstant());
                            matchEntity.setResult(getRandomPredictionScore());
                        }
                    }

                    matchEntity.setMatchDate(date);
                    matchEntityList.add(matchEntity);
                }
            }
        });
        matchEntityList.forEach(AppLifecycleService::persistGame);
        LOGGER.info("GameEntityList: persisted " + matchEntityList.size() + " Games");
        return matchEntityList;
    }

    private long getRandomMinutes() {
        return (long) (Math.random() * 60);
    }


    private long getRandomHours() {
        return (long) (Math.random() * 24);
    }

    private List<GroupEntity> initGroups(List<TeamEntity> teamList) {
        LOGGER.info("Init Groups...");
        List<GroupEntity> groupList = new ArrayList<>();
        GroupEntity groupA = new GroupEntity();
        groupA.setGroupName("A");
        groupA.setTeams(new HashSet<>(teamList.subList(0, 4)));
        groupA.getTeams().forEach(team -> team.setGroupEntity(groupA));
        groupList.add(groupA);
        GroupEntity groupB = new GroupEntity();
        groupB.setGroupName("B");
        groupB.setTeams(new HashSet<>(teamList.subList(4, 8)));
        groupB.getTeams().forEach(team -> team.setGroupEntity(groupB));
        groupList.add(groupB);
        GroupEntity groupC = new GroupEntity();
        groupC.setGroupName("C");
        groupC.setTeams(new HashSet<>(teamList.subList(8, 12)));
        groupC.getTeams().forEach(team -> team.setGroupEntity(groupC));
        groupList.add(groupC);
        GroupEntity groupD = new GroupEntity();
        groupD.setGroupName("D");
        groupD.setTeams(new HashSet<>(teamList.subList(12, 16)));
        groupD.getTeams().forEach(team -> team.setGroupEntity(groupD));
        groupList.add(groupD);
        GroupEntity groupE = new GroupEntity();
        groupE.setGroupName("E");
        groupE.setTeams(new HashSet<>(teamList.subList(16, 20)));
        groupE.getTeams().forEach(team -> team.setGroupEntity(groupE));
        groupList.add(groupE);
        GroupEntity groupF = new GroupEntity();
        groupF.setGroupName("F");
        groupF.setTeams(new HashSet<>(teamList.subList(20, 24)));
        groupF.getTeams().forEach(team -> team.setGroupEntity(groupF));
        groupList.add(groupF);
        GroupEntity groupG = new GroupEntity();
        groupG.setGroupName("G");
        groupG.setTeams(new HashSet<>(teamList.subList(24, 28)));
        groupG.getTeams().forEach(team -> team.setGroupEntity(groupG));
        groupList.add(groupG);
        GroupEntity groupH = new GroupEntity();
        groupH.setGroupName("H");
        groupH.setTeams(new HashSet<>(teamList.subList(28, 32)));
        groupH.getTeams().forEach(team -> team.setGroupEntity(groupH));
        groupList.add(groupH);

        groupList.parallelStream().forEach(AppLifecycleService::persistGroup);
        LOGGER.info("Groups persisted");

        return groupList;
    }

    private List<TeamEntity> initTeams() {
        //Create Team of FIFA World Cup 2022
        TeamEntity germany = new TeamEntity("Germany", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Flag_of_Germany.svg/1200px-Flag_of_Germany.svg.png");
        TeamEntity brazil = new TeamEntity("Brazil", "https://upload.wikimedia.org/wikipedia/commons/0/05/Flag_of_Brazil.svg");
        TeamEntity argentina = new TeamEntity("Argentina", "https://upload.wikimedia.org/wikipedia/commons/1/1a/Flag_of_Argentina.svg");
        TeamEntity france = new TeamEntity("France", "https://upload.wikimedia.org/wikipedia/en/c/c3/Flag_of_France.svg");
        TeamEntity england = new TeamEntity("England", "https://upload.wikimedia.org/wikipedia/en/b/be/Flag_of_England.svg");
        TeamEntity spain = new TeamEntity("Spain", "https://upload.wikimedia.org/wikipedia/en/9/9a/Flag_of_Spain.svg");
        TeamEntity portugal = new TeamEntity("Portugal", "https://upload.wikimedia.org/wikipedia/commons/5/5c/Flag_of_Portugal.svg");
        TeamEntity belgium = new TeamEntity("Belgium", "https://upload.wikimedia.org/wikipedia/commons/6/65/Flag_of_Belgium.svg");
        TeamEntity uruguay = new TeamEntity("Uruguay", "https://upload.wikimedia.org/wikipedia/commons/f/fe/Flag_of_Uruguay.svg");
        TeamEntity italy = new TeamEntity("Italy", "https://upload.wikimedia.org/wikipedia/en/0/03/Flag_of_Italy.svg");
        TeamEntity poland = new TeamEntity("Poland", "https://upload.wikimedia.org/wikipedia/en/1/12/Flag_of_Poland.svg");
        TeamEntity switzerland = new TeamEntity("Switzerland", "https://upload.wikimedia.org/wikipedia/commons/0/08/Flag_of_Switzerland_%28Pantone%29.svg");
        TeamEntity denmark = new TeamEntity("Denmark", "https://upload.wikimedia.org/wikipedia/commons/9/9c/Flag_of_Denmark.svg");
        TeamEntity croatia = new TeamEntity("Croatia", "https://upload.wikimedia.org/wikipedia/commons/1/1b/Flag_of_Croatia.svg");
        TeamEntity sweden = new TeamEntity("Sweden", "https://upload.wikimedia.org/wikipedia/en/4/4c/Flag_of_Sweden.svg");
        TeamEntity mexico = new TeamEntity("Mexico", "https://upload.wikimedia.org/wikipedia/commons/f/fc/Flag_of_Mexico.svg");
        TeamEntity austria = new TeamEntity("Austria", "https://upload.wikimedia.org/wikipedia/commons/4/41/Flag_of_Austria.svg");
        TeamEntity netherlands = new TeamEntity("Netherlands", "https://upload.wikimedia.org/wikipedia/commons/2/20/Flag_of_the_Netherlands.svg");
        TeamEntity turkey = new TeamEntity("Turkey", "https://upload.wikimedia.org/wikipedia/commons/b/b4/Flag_of_Turkey.svg");
        TeamEntity northMacedonia = new TeamEntity("North Macedonia", "https://upload.wikimedia.org/wikipedia/commons/7/79/Flag_of_North_Macedonia.svg");
        TeamEntity wales = new TeamEntity("Wales", "https://upload.wikimedia.org/wikipedia/commons/d/dc/Flag_of_Wales.svg");
        TeamEntity slovakia = new TeamEntity("Slovakia", "https://upload.wikimedia.org/wikipedia/commons/e/e6/Flag_of_Slovakia.svg");
        TeamEntity slovenia = new TeamEntity("Slovenia", "https://upload.wikimedia.org/wikipedia/commons/f/f0/Flag_of_Slovenia.svg");
        TeamEntity ukraine = new TeamEntity("Ukraine", "https://upload.wikimedia.org/wikipedia/commons/4/49/Flag_of_Ukraine.svg");
        TeamEntity finland = new TeamEntity("Finland", "https://upload.wikimedia.org/wikipedia/commons/b/bc/Flag_of_Finland.svg");
        TeamEntity hungary = new TeamEntity("Hungary", "https://upload.wikimedia.org/wikipedia/commons/c/c1/Flag_of_Hungary.svg");
        TeamEntity russia = new TeamEntity("Russia", "https://upload.wikimedia.org/wikipedia/en/f/f3/Flag_of_Russia.svg");
        TeamEntity czechRepublic = new TeamEntity("Czech Republic", "https://upload.wikimedia.org/wikipedia/commons/c/cb/Flag_of_the_Czech_Republic.svg");
        TeamEntity scotland = new TeamEntity("Scotland", "https://upload.wikimedia.org/wikipedia/commons/1/10/Flag_of_Scotland.svg");
        TeamEntity romania = new TeamEntity("Romania", "https://upload.wikimedia.org/wikipedia/commons/7/73/Flag_of_Romania.svg");
        TeamEntity bulgaria = new TeamEntity("Bulgaria", "https://upload.wikimedia.org/wikipedia/commons/9/9a/Flag_of_Bulgaria.svg");
        TeamEntity estonia = new TeamEntity("Estonia", "https://upload.wikimedia.org/wikipedia/commons/8/8f/Flag_of_Estonia.svg");

        // Add all Team to List
        List<TeamEntity> teamEntityList = new ArrayList<>();
        teamEntityList.add(germany);
        teamEntityList.add(brazil);
        teamEntityList.add(argentina);
        teamEntityList.add(france);
        teamEntityList.add(england);
        teamEntityList.add(spain);
        teamEntityList.add(portugal);
        teamEntityList.add(belgium);
        teamEntityList.add(uruguay);
        teamEntityList.add(italy);
        teamEntityList.add(poland);
        teamEntityList.add(switzerland);
        teamEntityList.add(denmark);
        teamEntityList.add(croatia);
        teamEntityList.add(sweden);
        teamEntityList.add(mexico);
        teamEntityList.add(austria);
        teamEntityList.add(netherlands);
        teamEntityList.add(turkey);
        teamEntityList.add(northMacedonia);
        teamEntityList.add(wales);
        teamEntityList.add(slovakia);
        teamEntityList.add(slovenia);
        teamEntityList.add(ukraine);
        teamEntityList.add(finland);
        teamEntityList.add(hungary);
        teamEntityList.add(russia);
        teamEntityList.add(czechRepublic);
        teamEntityList.add(scotland);
        teamEntityList.add(romania);
        teamEntityList.add(bulgaria);
        teamEntityList.add(estonia);

        LOGGER.info("Team Initialization done");
        return teamEntityList;
    }


    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
