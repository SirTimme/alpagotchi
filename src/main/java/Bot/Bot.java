package Bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {

    public static void main(String[] args) throws LoginException {

        JDABuilder.createDefault(
                Config.get("TOKEN"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES
        )
                .disableCache(
                        CacheFlag.VOICE_STATE,
                        CacheFlag.EMOTE,
                        CacheFlag.CLIENT_STATUS
                )
                .addEventListeners(new CommandListener())
                .setActivity(Activity.playing("Alpacas <3"))
                .build();
    }
}
