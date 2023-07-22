package bot.commands.member;

import bot.commands.UserSlashCommand;
import bot.models.User;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Locale;

public class Balance extends UserSlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        event.reply(Responses.getLocalizedResponse("balance.successful", locale, user.getInventory().getCurrency())).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("balance", "Shows your balance of fluffies")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Ruft dein Guthaben an Fluffies ab");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}