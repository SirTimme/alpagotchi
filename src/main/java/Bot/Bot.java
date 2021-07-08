package Bot;

import Bot.Events.EventHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    public static void main(String[] args) {
        try {
            JDABuilder.create(
                    Config.get("TOKEN"),
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_MEMBERS
            )
                      .disableCache(
                              CacheFlag.ACTIVITY,
                              CacheFlag.VOICE_STATE,
                              CacheFlag.EMOTE,
                              CacheFlag.CLIENT_STATUS,
                              CacheFlag.ONLINE_STATUS
                      )
                      .addEventListeners(new EventHandler())
                      .setActivity(Activity.playing(Config.get("PREFIX") + "help | \uD83E\uDD99 Alpacas"))
                      .build();
        } catch (LoginException error) {
            LOGGER.error(error.getMessage());
        }
    }
}
