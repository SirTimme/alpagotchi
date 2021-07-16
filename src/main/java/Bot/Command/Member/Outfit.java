package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Database.IDatabase;
import Bot.Models.Entry;
import Bot.Utils.Emote;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Outfit implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        final Entry entry = IDatabase.INSTANCE.getEntry(authorID);

        if (entry == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final String outfit = event.getOption("outfit").getAsString();

        entry.getAlpaca().setOutfit(outfit);

        IDatabase.INSTANCE.setEntry(authorID, entry);

        event.reply("\uD83D\uDC54 The outfit of your alpaca has been set to **" + outfit + "**").queue();
    }
}
