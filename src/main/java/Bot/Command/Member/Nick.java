package Bot.Command.Member;

import Bot.Command.IUserCommand;
import Bot.Models.DBUser;
import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Nick implements IUserCommand {
    @Override
    public void execute(SlashCommandEvent event, DBUser user) {
        final String nickname = event.getOption("nickname").getAsString();
        if (nickname.length() > 256) {
            event.reply(Emote.REDCROSS + " The nickname must not exceed **256** characters")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        user.getAlpaca().setNickname(nickname);
        IDatabase.INSTANCE.setUser(user.getId(), user);

        event.reply("\uD83D\uDD8A The nickname of your alpaca has been set to **" + nickname + "**").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("nick", "Gives your alpaca a new nickname")
                .addOptions(
                        new OptionData(STRING, "nickname", "The new nickname of the alpaca", true)
                );
    }
}
