package bot;

import bot.db.IDatabase;
import bot.events.EventHandler;
import bot.utils.Env;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Bot {
    public static void main(final String[] args) {
        IDatabase.INSTANCE.connect();

        JDABuilder.createLight(Env.get("TOKEN"))
                  .addEventListeners(new EventHandler())
                  .setActivity(Activity.playing("/help | \uD83E\uDD99 Alpacas"))
                  .build();
    }
}