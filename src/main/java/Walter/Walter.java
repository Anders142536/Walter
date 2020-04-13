package Walter;

import Walter.exceptions.PathNotFoundException;
import Walter.exceptions.TokenNotFoundException;
import jdk.nashorn.internal.parser.Token;
import net.dv8tion.jda.api.*;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Walter {

    final public static String VERSION = "2.3.1";   //REWRITES . NEW FEATURES . BUILD

    public static void main(String[] args) {
        //TODO add logging
        try {
            String path = getLocation();
            String token = fetchToken(path);

            JDA bot = new JDABuilder()
                    .setToken(token)
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
        String path = jarLocation.getParent();
        if (path == null || path.equals("")) throw new PathNotFoundException();
        return path + "/";
    }
}
