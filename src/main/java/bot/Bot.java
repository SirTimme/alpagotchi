package bot;

import bot.events.EventHandler;
import bot.utils.Env;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    public static void main(final String[] args) {
        try {
            JDABuilder.createLight(Env.get("TOKEN"))
                      .addEventListeners(new EventHandler())
                      .setActivity(Activity.playing("/help | \uD83E\uDD99 Alpacas"))
                      .build();
        }
        catch (final LoginException error) {
            LOGGER.error(error.getMessage());
        }
    }
}
