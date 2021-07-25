package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Database.IDatabase;
import Bot.Models.User;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Outfit implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        final User user = IDatabase.INSTANCE.getUser(authorID);
        if (user == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final String outfit = event.getOption("outfit").getAsString();

        user.getAlpaca().setOutfit(outfit);

        IDatabase.INSTANCE.setUser(authorID, user);

        event.reply("\uD83D\uDC54 The outfit of your alpaca has been set to **" + outfit + "**").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("outfit", "Changes the appearance of your alpaca").addOptions(
                new OptionData(STRING, "outfit", "The new outfit of your alpaca", true)
                        .addChoices(
                                new Command.Choice("default", "default"),
                                new Command.Choice("gentleman", "gentleman"),
                                new Command.Choice("lady", "lady")
                        )
        );
    }
}
