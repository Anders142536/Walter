package Walter;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Walter {

    public static void main(String[] args)  throws Exception {

        JDA api = new JDABuilder(AccountType.BOT)
                .setToken(fetchToken())
                .addEventListener(new Listener())
                .build();

    }

    //TODO: read the token from a file
    private static String fetchToken() {
        return "";
    }
}
