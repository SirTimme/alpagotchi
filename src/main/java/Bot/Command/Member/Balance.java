package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.User;
import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import Bot.Utils.Language;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Balance implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        User user = IDatabase.INSTANCE.getUser(authorID);
        if (user == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }
        final int balance = user.getInventory().getCurrency();

        event.reply("\uD83D\uDCB5 Your current balance is **" + Language.handle(balance, "fluffy") + "**").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("balance", "Shows your fluffy balance");
    }
}
