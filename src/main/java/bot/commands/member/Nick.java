package bot.commands.member;

import bot.commands.IUserCommand;
import bot.models.Entry;
import bot.utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Nick implements IUserCommand {
    @Override
    public Entry execute(SlashCommandEvent event, Entry user) {
        final String nickname = event.getOption("nickname").getAsString();
        if (nickname.length() > 256) {
            event.reply(Emote.REDCROSS + " The nickname must not exceed **250** characters")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        user.setNickname(nickname);

        event.reply("\uD83D\uDD8A The nickname of your alpaca has been set to **" + nickname + "**").queue();

        return user;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("nick", "Gives your alpaca a new nickname")
                .addOptions(
                        new OptionData(STRING, "nickname", "The new nickname of the alpaca", true)
                );
    }
}
