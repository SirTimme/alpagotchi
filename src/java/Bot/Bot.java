package Bot;

import Bot.Events.MessageListener;
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
                  GatewayIntent.GUILD_MESSAGES
            )
                  .disableCache(
                        CacheFlag.ACTIVITY,
                        CacheFlag.VOICE_STATE,
                        CacheFlag.EMOTE,
                        CacheFlag.CLIENT_STATUS
                  )
                    .addEventListeners(new MessageListener())
                    .setActivity(Activity.playing(Config.get("PREFIX") + "help | Alpacas \uD83D\uDC96 \uD83E\uDD99"))
                    .build();

        } catch (LoginException error) {
            LOGGER.error(error.getMessage());
        }
    }
}
