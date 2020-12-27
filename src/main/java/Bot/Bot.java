package Bot;

import Bot.Handler.MessageListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    public static void main(String[] args) {

        try {
            JDABuilder
                    .create(Config.get("TOKEN"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                    .addEventListeners(new MessageListener())
                    .setActivity(Activity.playing("a!help | Alpacas \uD83D\uDC96 \uD83E\uDD99"))
                    .build();

        } catch (LoginException error) {
            LOGGER.error(error.getMessage());
        }
    }
}
