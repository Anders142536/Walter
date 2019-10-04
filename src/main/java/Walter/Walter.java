package Walter;

import net.dv8tion.jda.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;

public class Walter {

    public static void main(String[] args)  throws Exception {
        //TODO add logging
        String path = getLocation();

        if (path.equals("")) {
            System.out.println("path was not found");
            return;
        }

        String token = fetchToken(path);
        if (token == null || token.equals("")) {
            System.out.println("token was not found");
        }

        System.out.println("path: " + path +
                            "\ntoken: >" + token + "<");
        JDA api = new JDABuilder(AccountType.BOT)
                .setToken(token)
//                .addEventListeners(new Listener())
                .build();
    }

    //TODO: read the token from a file
    private static String fetchToken(String path) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(path + "token"));
            return file.readLine();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getLocation() {
        File path = new File (Walter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return path.getParent() + "/";
    }
}
