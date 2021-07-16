package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.User;
import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Nick implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        User user = IDatabase.INSTANCE.getUser(authorID);

        if (user == null) {
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

        user.getAlpaca().setNickname(nickname);

        IDatabase.INSTANCE.setUser(authorID, user);

        event.reply("\uD83D\uDD8A The nickname of your alpaca has been set to **" + nickname + "**").queue();
    }
}
