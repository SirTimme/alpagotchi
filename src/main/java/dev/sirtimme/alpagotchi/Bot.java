package dev.sirtimme.alpagotchi;

import dev.sirtimme.alpagotchi.db.IDatabase;
import dev.sirtimme.alpagotchi.events.EventHandler;
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