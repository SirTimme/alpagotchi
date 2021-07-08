package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Database.IDatabase;
import Bot.Models.Entry;
import Bot.Utils.Emote;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Sleep implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        Entry entry = IDatabase.INSTANCE.getEntry(authorID);

        if (entry == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        int energy = entry.getAlpaca().getEnergy();

        if (energy == 100) {
            event.reply(Emote.REDCROSS + " The energy of your alpaca is already at the maximum")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int duration = (int) event.getOption("duration").getAsLong();

        if (duration < 1 || duration > 100) {
            event.reply(Emote.REDCROSS + " Please enter a number between 1 - 100")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        energy = energy + duration > 100 ? 100 - energy : duration;
        final long cooldown = System.currentTimeMillis() + 1000L * 60 * energy;

        IDatabase.INSTANCE.setEntry(authorID, Stat.ENERGY, energy);
        IDatabase.INSTANCE.setEntry(authorID, Stat.SLEEP, cooldown);

        event.reply("\uD83D\uDCA4 Your alpaca goes to bed for **" + energy + "** minutes and rests well **Energy + " + energy + "**")
             .queue();

    }
}
