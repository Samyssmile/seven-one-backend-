package main.service.migration;

import io.quarkus.runtime.configuration.ProfileManager;
import main.entity.*;
import main.request.CreateUserRequest;
import main.service.PredictionService;
import main.service.UserService;
import main.utility.NameGenerator;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.IntStream;

public class ProdDataMigration extends DataPersistance implements IDataMigration {
    private static final Logger LOGGER = Logger.getLogger(ProdDataMigration.class);
    private final UserService userService;
    private final PredictionService predictionService;
    private final List<TeamEntity> teamEntityList = new ArrayList<>();
    private final List<GroupEntity> groupList = new ArrayList<>();
    private final List<MatchEntity> matchEntityList = new ArrayList<>();
    private final List<UserEntity> userEntityList = new ArrayList<>();
    private final DataPersistance dataPersistance;

    public ProdDataMigration(UserService userService, PredictionService predictionService, DataPersistance dataPersistance) {
        this.dataPersistance = dataPersistance;
        this.predictionService = predictionService;
        this.userService = userService;


    }

    @Override
    public void migrate() {
        migrateTeams();
        migrateGroups();
        migrateMatchSchedule();

        String activeProfile = ProfileManager.getActiveProfile();
        migrateUsers(activeProfile.equals("test") ? 0 : 600);
        migratePredictions(userEntityList, matchEntityList);

        migratePredictions(userEntityList, matchEntityList);

        LOGGER.info("Migration finished");
    }

    @Override
    public void migrateTeams() {
        // Group A
        TeamEntity qatar = new TeamEntity("Qatar", "https://upload.wikimedia.org/wikipedia/commons/6/65/Flag_of_Qatar.svg");
        TeamEntity ecuador = new TeamEntity("Ecuador", "https://upload.wikimedia.org/wikipedia/commons/e/e8/Flag_of_Ecuador.svg");
        TeamEntity senegal = new TeamEntity("Senegal", "https://upload.wikimedia.org/wikipedia/commons/f/fd/Flag_of_Senegal.svg");
        TeamEntity netherlands = new TeamEntity("Netherlands", "https://upload.wikimedia.org/wikipedia/commons/2/20/Flag_of_the_Netherlands.svg");


        // Group B
        TeamEntity england = new TeamEntity("England", "https://upload.wikimedia.org/wikipedia/en/b/be/Flag_of_England.svg");
        TeamEntity irIran = new TeamEntity("Iran", "https://upload.wikimedia.org/wikipedia/commons/c/ca/Flag_of_Iran.svg");
        TeamEntity usa = new TeamEntity("USA", "https://upload.wikimedia.org/wikipedia/commons/a/a4/Flag_of_the_United_States.svg");
        TeamEntity wales = new TeamEntity("Wales", "https://upload.wikimedia.org/wikipedia/commons/d/dc/Flag_of_Wales.svg");

        // Group C
        TeamEntity argentina = new TeamEntity("Argentina", "https://upload.wikimedia.org/wikipedia/commons/1/1a/Flag_of_Argentina.svg");
        TeamEntity saudiArabia = new TeamEntity("Saudi Arabia", "https://upload.wikimedia.org/wikipedia/commons/0/0d/Flag_of_Saudi_Arabia.svg");
        TeamEntity mexico = new TeamEntity("Mexico", "https://upload.wikimedia.org/wikipedia/commons/f/fc/Flag_of_Mexico.svg");
        TeamEntity poland = new TeamEntity("Poland", "https://upload.wikimedia.org/wikipedia/en/1/12/Flag_of_Poland.svg");

        // Group D
        TeamEntity france = new TeamEntity("France", "https://upload.wikimedia.org/wikipedia/en/c/c3/Flag_of_France.svg");
        TeamEntity australia = new TeamEntity("Australia", "https://upload.wikimedia.org/wikipedia/commons/8/88/Flag_of_Australia_%28converted%29.svg");
        TeamEntity denmark = new TeamEntity("Denmark", "https://upload.wikimedia.org/wikipedia/commons/9/9c/Flag_of_Denmark.svg");
        TeamEntity tunisia = new TeamEntity("Tunisia", "https://upload.wikimedia.org/wikipedia/commons/c/ce/Flag_of_Tunisia.svg");

        // Group E
        TeamEntity spain = new TeamEntity("Spain", "https://upload.wikimedia.org/wikipedia/en/9/9a/Flag_of_Spain.svg");
        TeamEntity costaRica = new TeamEntity("Costa Rica", "https://upload.wikimedia.org/wikipedia/commons/b/bc/Flag_of_Costa_Rica.svg");
        TeamEntity germany = new TeamEntity("Germany", "https://upload.wikimedia.org/wikipedia/en/b/ba/Flag_of_Germany.svg");
        TeamEntity japan = new TeamEntity("Japan", "https://upload.wikimedia.org/wikipedia/en/9/9e/Flag_of_Japan.svg");

        // Group F
        TeamEntity belgium = new TeamEntity("Belgium", "https://upload.wikimedia.org/wikipedia/commons/6/65/Flag_of_Belgium.svg");
        TeamEntity canada = new TeamEntity("Canada", "https://upload.wikimedia.org/wikipedia/commons/d/d9/Flag_of_Canada.svg");
        TeamEntity morocco = new TeamEntity("Morocco", "https://upload.wikimedia.org/wikipedia/commons/2/2c/Flag_of_Morocco.svg");
        TeamEntity croatia = new TeamEntity("Croatia", "https://upload.wikimedia.org/wikipedia/commons/1/1b/Flag_of_Croatia.svg");

        // Group G
        TeamEntity brazil = new TeamEntity("Brazil", "https://upload.wikimedia.org/wikipedia/commons/0/05/Flag_of_Brazil.svg");
        TeamEntity serbia = new TeamEntity("Serbia", "https://upload.wikimedia.org/wikipedia/commons/f/ff/Flag_of_Serbia.svg");
        TeamEntity switzerland = new TeamEntity("Switzerland", "https://upload.wikimedia.org/wikipedia/commons/0/08/Flag_of_Switzerland_%28Pantone%29.svg");
        TeamEntity cameroon = new TeamEntity("Cameroon", "https://upload.wikimedia.org/wikipedia/commons/4/4f/Flag_of_Cameroon.svg");

        // Group H
        TeamEntity portugal = new TeamEntity("Portugal", "https://upload.wikimedia.org/wikipedia/commons/5/5c/Flag_of_Portugal.svg");
        TeamEntity ghana = new TeamEntity("Ghana", "https://upload.wikimedia.org/wikipedia/commons/1/19/Flag_of_Ghana.svg");
        TeamEntity uruguay = new TeamEntity("Uruguay", "https://upload.wikimedia.org/wikipedia/commons/f/fe/Flag_of_Uruguay.svg");
        TeamEntity koreaRepublic = new TeamEntity("South Korea", "https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg");

        // Add all Team to List
        teamEntityList.add(qatar);
        teamEntityList.add(ecuador);
        teamEntityList.add(senegal);
        teamEntityList.add(netherlands);

        teamEntityList.add(england);
        teamEntityList.add(irIran);
        teamEntityList.add(usa);
        teamEntityList.add(wales);

        teamEntityList.add(argentina);
        teamEntityList.add(saudiArabia);
        teamEntityList.add(mexico);
        teamEntityList.add(poland);

        teamEntityList.add(france);
        teamEntityList.add(australia);
        teamEntityList.add(denmark);
        teamEntityList.add(tunisia);

        teamEntityList.add(spain);
        teamEntityList.add(costaRica);
        teamEntityList.add(germany);
        teamEntityList.add(japan);

        teamEntityList.add(belgium);
        teamEntityList.add(canada);
        teamEntityList.add(morocco);
        teamEntityList.add(croatia);

        teamEntityList.add(brazil);
        teamEntityList.add(serbia);
        teamEntityList.add(switzerland);
        teamEntityList.add(cameroon);

        teamEntityList.add(portugal);
        teamEntityList.add(ghana);
        teamEntityList.add(uruguay);
        teamEntityList.add(koreaRepublic);

        LOGGER.info("Team Initialization done");
    }

    private void createGroup(String name, Set<TeamEntity> teams) {
        GroupEntity group = new GroupEntity();
        group.setGroupName(name);
        group.setTeams(teams);
        group.getTeams().forEach(team -> team.setGroupEntity(group));
        groupList.add(group);
    }

    @Override
    public void migrateGroups() {
        LOGGER.info("Init Groups...");
        createGroup("A", new HashSet<>(teamEntityList.subList(0, 4)));
        createGroup("B", new HashSet<>(teamEntityList.subList(4, 8)));
        createGroup("C", new HashSet<>(teamEntityList.subList(8, 12)));
        createGroup("D", new HashSet<>(teamEntityList.subList(12, 16)));
        createGroup("E", new HashSet<>(teamEntityList.subList(16, 20)));
        createGroup("F", new HashSet<>(teamEntityList.subList(20, 24)));
        createGroup("G", new HashSet<>(teamEntityList.subList(24, 28)));
        createGroup("H", new HashSet<>(teamEntityList.subList(28, 32)));

        dataPersistance.persistGroupList(groupList);
    }

    @Override
    public void migrateMatchSchedule() {
        // 20.11.2022
        MatchEntity qatarVsEcuador = new MatchEntity();
        qatarVsEcuador.setFirstTeam(getTeamByName("Qatar"));
        qatarVsEcuador.setSecondTeam(getTeamByName("Ecuador"));
        qatarVsEcuador.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 20, 17, 0).getTime());
        qatarVsEcuador.setGroupName("A");
        matchEntityList.add(qatarVsEcuador);


        // 21.11.2022
        MatchEntity englandVsIran = new MatchEntity();
        englandVsIran.setFirstTeam(getTeamByName("England"));
        englandVsIran.setSecondTeam(getTeamByName("Iran"));
        englandVsIran.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 21, 14, 0).getTime());
        englandVsIran.setGroupName("B");
        matchEntityList.add(englandVsIran);

        MatchEntity senegalVsNetherlands = new MatchEntity();
        senegalVsNetherlands.setFirstTeam(getTeamByName("Senegal"));
        senegalVsNetherlands.setSecondTeam(getTeamByName("Netherlands"));
        senegalVsNetherlands.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 21, 17, 0).getTime());
        senegalVsNetherlands.setGroupName("A");
        matchEntityList.add(senegalVsNetherlands);

        MatchEntity usaVsWales = new MatchEntity();
        usaVsWales.setFirstTeam(getTeamByName("USA"));
        usaVsWales.setSecondTeam(getTeamByName("Wales"));
        usaVsWales.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 21, 20, 0).getTime());
        usaVsWales.setGroupName("B");
        matchEntityList.add(usaVsWales);

        // 22.11.2022
        MatchEntity argentinaVsSaudiArabia = new MatchEntity();
        argentinaVsSaudiArabia.setFirstTeam(getTeamByName("Argentina"));
        argentinaVsSaudiArabia.setSecondTeam(getTeamByName("Saudi Arabia"));
        argentinaVsSaudiArabia.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 22, 11, 0).getTime());
        argentinaVsSaudiArabia.setGroupName("C");
        matchEntityList.add(argentinaVsSaudiArabia);

        MatchEntity denmarkVsTunisia = new MatchEntity();
        denmarkVsTunisia.setFirstTeam(getTeamByName("Denmark"));
        denmarkVsTunisia.setSecondTeam(getTeamByName("Tunisia"));
        denmarkVsTunisia.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 22, 14, 0).getTime());
        denmarkVsTunisia.setGroupName("D");
        matchEntityList.add(denmarkVsTunisia);

        MatchEntity mexicoVsPoland = new MatchEntity();
        mexicoVsPoland.setFirstTeam(getTeamByName("Mexico"));
        mexicoVsPoland.setSecondTeam(getTeamByName("Poland"));
        mexicoVsPoland.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 22, 17, 0).getTime());
        mexicoVsPoland.setGroupName("C");
        matchEntityList.add(mexicoVsPoland);

        MatchEntity franceVsAustralia = new MatchEntity();
        franceVsAustralia.setFirstTeam(getTeamByName("France"));
        franceVsAustralia.setSecondTeam(getTeamByName("Australia"));
        franceVsAustralia.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 22, 20, 0).getTime());
        franceVsAustralia.setGroupName("D");
        matchEntityList.add(franceVsAustralia);

        // 23.11.2022
        MatchEntity maroccoVsCroatia = new MatchEntity();
        maroccoVsCroatia.setFirstTeam(getTeamByName("Morocco"));
        maroccoVsCroatia.setSecondTeam(getTeamByName("Croatia"));
        maroccoVsCroatia.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 23, 11, 0).getTime());
        maroccoVsCroatia.setGroupName("F");
        matchEntityList.add(maroccoVsCroatia);

        MatchEntity germanyVsJapan = new MatchEntity();
        germanyVsJapan.setFirstTeam(getTeamByName("Germany"));
        germanyVsJapan.setSecondTeam(getTeamByName("Japan"));
        germanyVsJapan.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 23, 14, 0).getTime());
        germanyVsJapan.setGroupName("E");
        matchEntityList.add(germanyVsJapan);

        MatchEntity spainVsCostaRica = new MatchEntity();
        spainVsCostaRica.setFirstTeam(getTeamByName("Spain"));
        spainVsCostaRica.setSecondTeam(getTeamByName("Costa Rica"));
        spainVsCostaRica.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 23, 17, 0).getTime());
        spainVsCostaRica.setGroupName("E");
        matchEntityList.add(spainVsCostaRica);

        MatchEntity belgiumVsCanada = new MatchEntity();
        belgiumVsCanada.setFirstTeam(getTeamByName("Belgium"));
        belgiumVsCanada.setSecondTeam(getTeamByName("Canada"));
        belgiumVsCanada.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 23, 20, 0).getTime());
        belgiumVsCanada.setGroupName("F");
        matchEntityList.add(belgiumVsCanada);

        // 24.11.2022
        MatchEntity switzerlandVsCameroon = new MatchEntity();
        switzerlandVsCameroon.setFirstTeam(getTeamByName("Switzerland"));
        switzerlandVsCameroon.setSecondTeam(getTeamByName("Cameroon"));
        switzerlandVsCameroon.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 24, 11, 0).getTime());
        switzerlandVsCameroon.setGroupName("G");
        matchEntityList.add(switzerlandVsCameroon);

        MatchEntity uruguayVsSouthKorea = new MatchEntity();
        uruguayVsSouthKorea.setFirstTeam(getTeamByName("Uruguay"));
        uruguayVsSouthKorea.setSecondTeam(getTeamByName("South Korea"));
        uruguayVsSouthKorea.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 24, 14, 0).getTime());
        uruguayVsSouthKorea.setGroupName("H");
        matchEntityList.add(uruguayVsSouthKorea);

        MatchEntity portugalVsGhana = new MatchEntity();
        portugalVsGhana.setFirstTeam(getTeamByName("Portugal"));
        portugalVsGhana.setSecondTeam(getTeamByName("Ghana"));
        portugalVsGhana.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 24, 17, 0).getTime());
        portugalVsGhana.setGroupName("H");
        matchEntityList.add(portugalVsGhana);

        MatchEntity brazilVsSerbia = new MatchEntity();
        brazilVsSerbia.setFirstTeam(getTeamByName("Brazil"));
        brazilVsSerbia.setSecondTeam(getTeamByName("Serbia"));
        brazilVsSerbia.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 24, 20, 0).getTime());
        brazilVsSerbia.setGroupName("G");
        matchEntityList.add(brazilVsSerbia);

        // 25.11.2022
        MatchEntity walesVsIran = new MatchEntity();
        walesVsIran.setFirstTeam(getTeamByName("Wales"));
        walesVsIran.setSecondTeam(getTeamByName("Iran"));
        walesVsIran.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 25, 11, 0).getTime());
        walesVsIran.setGroupName("B");
        matchEntityList.add(walesVsIran);

        MatchEntity qatarVsSenegal = new MatchEntity();
        qatarVsSenegal.setFirstTeam(getTeamByName("Qatar"));
        qatarVsSenegal.setSecondTeam(getTeamByName("Senegal"));
        qatarVsSenegal.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 25, 14, 0).getTime());
        qatarVsSenegal.setGroupName("A");
        matchEntityList.add(qatarVsSenegal);

        MatchEntity netherlandVsEcuador = new MatchEntity();
        netherlandVsEcuador.setFirstTeam(getTeamByName("Netherlands"));
        netherlandVsEcuador.setSecondTeam(getTeamByName("Ecuador"));
        netherlandVsEcuador.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 25, 17, 0).getTime());
        netherlandVsEcuador.setGroupName("A");
        matchEntityList.add(netherlandVsEcuador);

        MatchEntity englandVsUsa = new MatchEntity();
        englandVsUsa.setFirstTeam(getTeamByName("England"));
        englandVsUsa.setSecondTeam(getTeamByName("USA"));
        englandVsUsa.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 25, 20, 0).getTime());
        englandVsUsa.setGroupName("B");
        matchEntityList.add(englandVsUsa);

        // 26.11.2022
        MatchEntity tunisiaVsAustralia = new MatchEntity();
        tunisiaVsAustralia.setFirstTeam(getTeamByName("Tunisia"));
        tunisiaVsAustralia.setSecondTeam(getTeamByName("Australia"));
        tunisiaVsAustralia.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 26, 11, 0).getTime());
        tunisiaVsAustralia.setGroupName("D");
        matchEntityList.add(tunisiaVsAustralia);

        MatchEntity polandVsSaudiArabia = new MatchEntity();
        polandVsSaudiArabia.setFirstTeam(getTeamByName("Poland"));
        polandVsSaudiArabia.setSecondTeam(getTeamByName("Saudi Arabia"));
        polandVsSaudiArabia.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 26, 14, 0).getTime());
        polandVsSaudiArabia.setGroupName("C");
        matchEntityList.add(polandVsSaudiArabia);

        MatchEntity franceVsDenmark = new MatchEntity();
        franceVsDenmark.setFirstTeam(getTeamByName("France"));
        franceVsDenmark.setSecondTeam(getTeamByName("Denmark"));
        franceVsDenmark.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 26, 17, 0).getTime());
        franceVsDenmark.setGroupName("D");
        matchEntityList.add(franceVsDenmark);

        MatchEntity argentinaVsMexico = new MatchEntity();
        argentinaVsMexico.setFirstTeam(getTeamByName("Argentina"));
        argentinaVsMexico.setSecondTeam(getTeamByName("Mexico"));
        argentinaVsMexico.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 26, 20, 0).getTime());
        argentinaVsMexico.setGroupName("C");
        matchEntityList.add(argentinaVsMexico);

        // 27.11.2022
        MatchEntity japanVsCostaRica = new MatchEntity();
        japanVsCostaRica.setFirstTeam(getTeamByName("Japan"));
        japanVsCostaRica.setSecondTeam(getTeamByName("Costa Rica"));
        japanVsCostaRica.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 27, 11, 0).getTime());
        japanVsCostaRica.setGroupName("E");
        matchEntityList.add(japanVsCostaRica);

        MatchEntity belgiumVsMarocco = new MatchEntity();
        belgiumVsMarocco.setFirstTeam(getTeamByName("Belgium"));
        belgiumVsMarocco.setSecondTeam(getTeamByName("Morocco"));
        belgiumVsMarocco.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 27, 14, 0).getTime());
        belgiumVsMarocco.setGroupName("F");
        matchEntityList.add(belgiumVsMarocco);

        MatchEntity croatiaVsCanada = new MatchEntity();
        croatiaVsCanada.setFirstTeam(getTeamByName("Croatia"));
        croatiaVsCanada.setSecondTeam(getTeamByName("Canada"));
        croatiaVsCanada.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 27, 17, 0).getTime());
        croatiaVsCanada.setGroupName("F");
        matchEntityList.add(croatiaVsCanada);

        MatchEntity spainVsGermany = new MatchEntity();
        spainVsGermany.setFirstTeam(getTeamByName("Spain"));
        spainVsGermany.setSecondTeam(getTeamByName("Germany"));
        spainVsGermany.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 27, 20, 0).getTime());
        spainVsGermany.setGroupName("E");
        matchEntityList.add(spainVsGermany);

        // 28.11.2022
        MatchEntity cameroonVsSerbia = new MatchEntity();
        cameroonVsSerbia.setFirstTeam(getTeamByName("Cameroon"));
        cameroonVsSerbia.setSecondTeam(getTeamByName("Serbia"));
        cameroonVsSerbia.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 28, 11, 0).getTime());
        cameroonVsSerbia.setGroupName("G");
        matchEntityList.add(cameroonVsSerbia);

        MatchEntity southKoreaVsGhana = new MatchEntity();
        southKoreaVsGhana.setFirstTeam(getTeamByName("South Korea"));
        southKoreaVsGhana.setSecondTeam(getTeamByName("Ghana"));
        southKoreaVsGhana.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 28, 14, 0).getTime());
        southKoreaVsGhana.setGroupName("H");
        matchEntityList.add(southKoreaVsGhana);

        MatchEntity brazilVsSwitzerland = new MatchEntity();
        brazilVsSwitzerland.setFirstTeam(getTeamByName("Brazil"));
        brazilVsSwitzerland.setSecondTeam(getTeamByName("Switzerland"));
        brazilVsSwitzerland.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 28, 17, 0).getTime());
        brazilVsSwitzerland.setGroupName("G");
        matchEntityList.add(brazilVsSwitzerland);

        MatchEntity portugalVsUruguay = new MatchEntity();
        portugalVsUruguay.setFirstTeam(getTeamByName("Portugal"));
        portugalVsUruguay.setSecondTeam(getTeamByName("Uruguay"));
        portugalVsUruguay.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 28, 20, 0).getTime());
        portugalVsUruguay.setGroupName("H");
        matchEntityList.add(portugalVsUruguay);

        // 29.11.2022
        MatchEntity ecuadorVsSenegal = new MatchEntity();
        ecuadorVsSenegal.setFirstTeam(getTeamByName("Ecuador"));
        ecuadorVsSenegal.setSecondTeam(getTeamByName("Senegal"));
        ecuadorVsSenegal.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 29, 16, 0).getTime());
        ecuadorVsSenegal.setGroupName("A");
        matchEntityList.add(ecuadorVsSenegal);

        MatchEntity netherlandsVsQatar = new MatchEntity();
        netherlandsVsQatar.setFirstTeam(getTeamByName("Netherlands"));
        netherlandsVsQatar.setSecondTeam(getTeamByName("Qatar"));
        netherlandsVsQatar.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 29, 16, 0).getTime());
        netherlandsVsQatar.setGroupName("A");
        matchEntityList.add(netherlandsVsQatar);

        MatchEntity iranVsUsa = new MatchEntity();
        iranVsUsa.setFirstTeam(getTeamByName("Iran"));
        iranVsUsa.setSecondTeam(getTeamByName("USA"));
        iranVsUsa.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 29, 20, 0).getTime());
        iranVsUsa.setGroupName("B");
        matchEntityList.add(iranVsUsa);

        MatchEntity walesVsEngland = new MatchEntity();
        walesVsEngland.setFirstTeam(getTeamByName("Wales"));
        walesVsEngland.setSecondTeam(getTeamByName("England"));
        walesVsEngland.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 29, 20, 0).getTime());
        walesVsEngland.setGroupName("B");
        matchEntityList.add(walesVsEngland);

        // 30.11.2022
        MatchEntity tunisiaVsFrance = new MatchEntity();
        tunisiaVsFrance.setFirstTeam(getTeamByName("Tunisia"));
        tunisiaVsFrance.setSecondTeam(getTeamByName("France"));
        tunisiaVsFrance.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 30, 16, 0).getTime());
        tunisiaVsFrance.setGroupName("D");
        matchEntityList.add(tunisiaVsFrance);

        MatchEntity australiaVsDenmark = new MatchEntity();
        australiaVsDenmark.setFirstTeam(getTeamByName("Australia"));
        australiaVsDenmark.setSecondTeam(getTeamByName("Denmark"));
        australiaVsDenmark.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 30, 16, 0).getTime());
        australiaVsDenmark.setGroupName("D");
        matchEntityList.add(australiaVsDenmark);

        MatchEntity polandVsArgentina = new MatchEntity();
        polandVsArgentina.setFirstTeam(getTeamByName("Poland"));
        polandVsArgentina.setSecondTeam(getTeamByName("Argentina"));
        polandVsArgentina.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 30, 20, 0).getTime());
        polandVsArgentina.setGroupName("C");
        matchEntityList.add(polandVsArgentina);

        MatchEntity saudiArabiaVsMexico = new MatchEntity();
        saudiArabiaVsMexico.setFirstTeam(getTeamByName("Saudi Arabia"));
        saudiArabiaVsMexico.setSecondTeam(getTeamByName("Mexico"));
        saudiArabiaVsMexico.setMatchDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 30, 20, 0).getTime());
        saudiArabiaVsMexico.setGroupName("C");
        matchEntityList.add(saudiArabiaVsMexico);

        // 1.12.2022
        MatchEntity croatiaVsBelgium = new MatchEntity();
        croatiaVsBelgium.setFirstTeam(getTeamByName("Croatia"));
        croatiaVsBelgium.setSecondTeam(getTeamByName("Belgium"));
        croatiaVsBelgium.setMatchDate(new GregorianCalendar(2022, Calendar.DECEMBER, 1, 16, 0).getTime());
        croatiaVsBelgium.setGroupName("F");
        matchEntityList.add(croatiaVsBelgium);

        MatchEntity canadaVsMarocco = new MatchEntity();
        canadaVsMarocco.setFirstTeam(getTeamByName("Canada"));
        canadaVsMarocco.setSecondTeam(getTeamByName("Morocco"));
        canadaVsMarocco.setMatchDate(new GregorianCalendar(2022, Calendar.DECEMBER, 1, 16, 0).getTime());
        canadaVsMarocco.setGroupName("F");
        matchEntityList.add(canadaVsMarocco);

        MatchEntity japanVsSpain = new MatchEntity();
        japanVsSpain.setFirstTeam(getTeamByName("Japan"));
        japanVsSpain.setSecondTeam(getTeamByName("Spain"));
        japanVsSpain.setMatchDate(new GregorianCalendar(2022, Calendar.DECEMBER, 1, 20, 0).getTime());
        japanVsSpain.setGroupName("E");
        matchEntityList.add(japanVsSpain);

        MatchEntity costaRicaVsGermany = new MatchEntity();
        costaRicaVsGermany.setFirstTeam(getTeamByName("Costa Rica"));
        costaRicaVsGermany.setSecondTeam(getTeamByName("Germany"));
        costaRicaVsGermany.setMatchDate(new GregorianCalendar(2022, Calendar.DECEMBER, 1, 20, 0).getTime());
        costaRicaVsGermany.setGroupName("E");
        matchEntityList.add(costaRicaVsGermany);

        // 2.12.2022
        MatchEntity southKoreaVsPortugal = new MatchEntity();
        southKoreaVsPortugal.setFirstTeam(getTeamByName("South Korea"));
        southKoreaVsPortugal.setSecondTeam(getTeamByName("Portugal"));
        southKoreaVsPortugal.setMatchDate(new GregorianCalendar(2022, Calendar.DECEMBER, 2, 16, 0).getTime());
        southKoreaVsPortugal.setGroupName("H");
        matchEntityList.add(southKoreaVsPortugal);

        MatchEntity ghanaVsUruguay = new MatchEntity();
        ghanaVsUruguay.setFirstTeam(getTeamByName("Ghana"));
        ghanaVsUruguay.setSecondTeam(getTeamByName("Uruguay"));
        ghanaVsUruguay.setMatchDate(new GregorianCalendar(2022, Calendar.DECEMBER, 2, 16, 0).getTime());
        ghanaVsUruguay.setGroupName("H");
        matchEntityList.add(ghanaVsUruguay);

        MatchEntity serbiaVsSwitzerland = new MatchEntity();
        serbiaVsSwitzerland.setFirstTeam(getTeamByName("Serbia"));
        serbiaVsSwitzerland.setSecondTeam(getTeamByName("Switzerland"));
        serbiaVsSwitzerland.setMatchDate(new GregorianCalendar(2022, Calendar.DECEMBER, 2, 20, 0).getTime());
        serbiaVsSwitzerland.setGroupName("G");
        matchEntityList.add(serbiaVsSwitzerland);

        MatchEntity cameroonVsBrazil = new MatchEntity();
        cameroonVsBrazil.setFirstTeam(getTeamByName("Cameroon"));
        cameroonVsBrazil.setSecondTeam(getTeamByName("Brazil"));
        cameroonVsBrazil.setMatchDate(new GregorianCalendar(2022, Calendar.DECEMBER, 2, 20, 0).getTime());
        cameroonVsBrazil.setGroupName("G");
        matchEntityList.add(cameroonVsBrazil);

        dataPersistance.persistMatches(matchEntityList);
    }

    @Override
    public void migrateUsers(int amountOfUsers) {
        LOGGER.info("Init Users...");
        IntStream.range(0, amountOfUsers).forEach(i -> {
            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setClientUuid(UUID.randomUUID());
            createUserRequest.setNickname(NameGenerator.generateName());
            createUserRequest.setScore(generateRandomScore());
            userService.saveNewUser(createUserRequest);
        });

        LOGGER.info("Users initialized");
    }

    @Override
    public void migratePredictions(List<UserEntity> userEntityList, List<MatchEntity> matchEntityList) {
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
            dataPersistance.persistPredictions(predictionDtos);
        });

        LOGGER.info("Predictions by users done...");
    }

    private TeamEntity getTeamByName(String name) {
        try {
            return teamEntityList.stream().filter(team -> team.getTeamName().equals(name)).findFirst().get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return null;
    }
}
