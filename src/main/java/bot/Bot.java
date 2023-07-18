package bot;

import bot.db.IDatabase;
import bot.events.EventHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Bot {
    public static void main(final String[] args) {
        // Init db
        IDatabase.INSTANCE.initDatabase();

        // Start the bot
        JDABuilder.createLight(System.getenv("TOKEN"))
                  .addEventListeners(new EventHandler())
                  .setActivity(Activity.playing("/help | \uD83E\uDD99 Alpacas"))
                  .build();
    }
}