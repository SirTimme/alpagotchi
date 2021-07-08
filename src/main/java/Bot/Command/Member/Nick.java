package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.Entry;
import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Nick implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        Entry entry = IDatabase.INSTANCE.getEntry(authorID);

        if (entry == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/ init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final String nickname = event.getOption("nickname").getAsString();

        if (nickname.length() > 256) {
            event.reply(Emote.REDCROSS + " The nickname must not exceed **256** characters")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        IDatabase.INSTANCE.setEntry(authorID, Stat.NICKNAME, nickname);

        event.reply("\uD83D\uDD8A The nickname of your alpaca has been set to **" + nickname + "**").queue();
    }
}
