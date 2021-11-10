package bot.commands.member;

import bot.commands.IStaticUserCommand;
import bot.models.Entry;
import bot.utils.Language;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Balance implements IStaticUserCommand {
    @Override
    public void execute(SlashCommandEvent event, Entry user) {
        final int balance = user.getCurrency();

        event.reply("\uD83D\uDCB5 Your current balance is **" + balance + " " + Language.handle(balance, "fluffy", "fluffies") + "**")
             .queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("balance", "Shows your fluffy balance");
    }
}
