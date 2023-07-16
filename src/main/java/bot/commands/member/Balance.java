package bot.commands.member;

import bot.commands.UserSlashCommand;
import bot.models.User;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.text.MessageFormat;
import java.util.Locale;

public class Balance extends UserSlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        final var format = new MessageFormat(Responses.getLocalizedResponse("balance", locale));
        final var msg = format.format(new Object[]{ user.getInventory().getCurrency() });

        event.reply(msg).queue();
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