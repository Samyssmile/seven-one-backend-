package main.utility;

import java.util.UUID;

public class Utility {

    private static final UUID game_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID game_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID game_3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private static final UUID game_4 = UUID.fromString("00000000-0000-0000-0000-000000000004");
    private static final UUID game_5 = UUID.fromString("00000000-0000-0000-0000-000000000005");
    private static final UUID game_6 = UUID.fromString("00000000-0000-0000-0000-000000000006");
    private static final UUID game_7 = UUID.fromString("00000000-0000-0000-0000-000000000007");
    private static final UUID game_8 = UUID.fromString("00000000-0000-0000-0000-000000000008");


    public static UUID getRandomMatchUUID() {
        int random = (int) (Math.random() * 8);
        switch (random) {
            case 0:
                return game_1;
            case 1:
                return game_2;
            case 2:
                return game_3;
            case 3:
                return game_4;
            case 4:
                return game_5;
            case 5:
                return game_6;
            case 6:
                return game_7;
            case 7:
                return game_8;
            default:
                return game_1;
        }

    }
}

