package bot;

import bot.events.EventHandler;
import bot.utils.Env;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Bot {
    public static void main(final String[] args) {
        JDABuilder.createLight(Env.get("TOKEN"))
                  .addEventListeners(new EventHandler())
                  .setActivity(Activity.playing("/help | \uD83E\uDD99 Alpacas"))
                  .build();
    }
}