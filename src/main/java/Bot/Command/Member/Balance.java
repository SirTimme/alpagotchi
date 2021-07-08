package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.Entry;
import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import Bot.Utils.Language;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Balance implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        Entry entry = IDatabase.INSTANCE.getEntry(authorID);

        if (entry == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int balance = entry.getInventory().getCurrency();

        event.reply("\uD83D\uDCB5 Your current balance is **" + Language.handle(balance, "fluffy") + "**")
             .queue();
    }
}
