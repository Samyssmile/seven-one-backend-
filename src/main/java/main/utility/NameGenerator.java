package main.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class NameGenerator {

    private static String[] Beginning = {"Kr", "Ca", "Ra", "Mrok", "Cru",
            "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol",
            "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro",
            "Mar", "Luk"};
    private static String[] Middle = {"air", "ir", "mi", "sor", "mee", "clo",
            "red", "cra", "ark", "arc", "miri", "lori", "cres", "mur", "zer",
            "marac", "zoir", "slamar", "salmar", "urak"};
    private static String[] End = {"d", "ed", "ark", "arc", "es", "er", "der",
            "tron", "med", "ure", "zur", "cred", "mur"};

    private static Random rand = new Random();

    public static String generateGenericName() {
        return Beginning[rand.nextInt(Beginning.length)] +
                Middle[rand.nextInt(Middle.length)] +
                End[rand.nextInt(End.length)];

    }

    public static String generateUsername() throws IOException {
        ClassLoader classLoader = NameGenerator.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("assets/names/name.txt")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            String[] names = text.split("\\r?\\n");
            return names[rand.nextInt(names.length)];
        }
    }

    public static String[] getRealNames() throws IOException {
        ClassLoader classLoader = NameGenerator.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("assets/names/name.txt")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            return text.split("\\r?\\n");
        }
    }

    public static String[] getUserNames() throws IOException {
        ClassLoader classLoader = NameGenerator.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("assets/names/usernamelist.txt")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            return text.split("\\r?\\n");
        }
    }

}
