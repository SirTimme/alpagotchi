package Bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot {

    public static void main(String[] args) throws LoginException {

        JDABuilder
                .create(Config.get("TOKEN"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .addEventListeners(new CommandListener())
                .setActivity(Activity.playing("Alpacas <3"))
                .build();
    }
}
