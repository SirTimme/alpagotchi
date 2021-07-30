package Bot.Command.Member;

import Bot.Command.IUserCommand;
import Bot.Models.DBUser;
import Bot.Utils.Language;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Balance implements IUserCommand {
    @Override
    public void execute(SlashCommandEvent event, DBUser user) {
        final int balance = user.getInventory().getCurrency();
        event.reply("\uD83D\uDCB5 Your current balance is **" + balance + " " + Language.handle(balance, "fluffy", "fluffies") + "**").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("balance", "Shows your fluffy balance");
    }
}
