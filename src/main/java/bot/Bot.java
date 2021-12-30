package bot;

import bot.events.EventHandler;
import bot.utils.Env;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

/**
 * Main entry point of the bot
 */
public class Bot {
    private final static Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    /**
     * Starts the bot
     *
     * @param args Optional args to be passed
     */
    public static void main(String[] args) {
        try {
            JDABuilder.create(Env.get("TOKEN"), GatewayIntent.GUILD_MESSAGES)
                      .disableCache(
                              CacheFlag.ACTIVITY,
                              CacheFlag.VOICE_STATE,
                              CacheFlag.EMOTE,
                              CacheFlag.CLIENT_STATUS,
                              CacheFlag.ONLINE_STATUS,
                              CacheFlag.MEMBER_OVERRIDES,
                              CacheFlag.ROLE_TAGS
                      )
                      .addEventListeners(new EventHandler())
                      .setActivity(Activity.playing("/help | \uD83E\uDD99 Alpacas"))
                      .build();
        }
        catch (LoginException error) {
            LOGGER.error(error.getMessage());
        }
    }
}
