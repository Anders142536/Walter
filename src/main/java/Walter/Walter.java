package Walter;

import Walter.exceptions.PathNotFoundException;
import Walter.exceptions.TokenNotFoundException;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Walter {

    final public static String VERSION = "2.7.0";   //REWRITES . NEW FEATURES . PURE BUGFIX
    public static String location;

    public static void main(String[] args) {
        //TODO add logging
        try {
            String path = getLocation();
            String token = fetchToken(path);
            JDA bot = JDABuilder.create(token,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_VOICE_STATES)
                    .addEventListeners(new Listener())
                    .build();
        } catch (Exception e) {
            System.out.println("Serious exception blocked bot from starting!");
            e.printStackTrace();
        }
    }

    private static String fetchToken(String path) throws Exception {
        BufferedReader file = new BufferedReader(new FileReader(path + "token"));
        String token = file.readLine();
        file.close();
        if (token == null || token.equals("")) throw new TokenNotFoundException();
        return token;
    }

    private static String getLocation() throws PathNotFoundException {
        File jarLocation = new File (Walter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        location = jarLocation.getParent();
        if (location == null || location.equals("")) throw new PathNotFoundException();
        location += "/";
        return location;
    }
}
