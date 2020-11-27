package Bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot {
    public static Bot INSTANCE;

    public static void main(String[] args) {
        try {
            new Bot();
        } catch (LoginException error) {
            error.printStackTrace();
        }
    }

    public Bot() throws LoginException {

        new JDABuilder()
                .setToken(Config.get("TOKEN"))
                .addEventListeners(new CommandListener())
                .setActivity(Activity.playing("Alpacas <3"))
                .build();
    }
}
