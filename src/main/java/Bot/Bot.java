package Bot;

import Bot.Events.EventHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {
    public static void main(String[] args) throws LoginException {
        JDABuilder.create(Config.get("TOKEN"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
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
}
