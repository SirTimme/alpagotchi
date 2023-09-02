package bot.commands.member;

import bot.commands.types.UserCommand;
import bot.models.user.User;
import bot.commands.types.CommandType;
import bot.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Locale;

public class Balance extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        event.reply(LocalizedResponse.get("balance.successful", locale, user.getInventory().getCurrency())).queue();
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