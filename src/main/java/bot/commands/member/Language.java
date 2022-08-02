package bot.commands.member;

import bot.commands.InfoCommand;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.text.MessageFormat;
import java.util.Locale;

public class Language extends InfoCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        final var userId = event.getUser().getId();

        final var menuLanguage = SelectMenu.create(userId + ":language")
                                           .setPlaceholder(Responses.get("languagePlaceholder", locale))
                                           .addOption(Responses.get("englishDisplayName", locale), "en", Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8"))
                                           .addOption(Responses.get("germanDisplayName", locale), "de", Emoji.fromUnicode("\uD83C\uDDE9\uD83C\uDDEA"))
                                           .build();

        final var format = new MessageFormat(Responses.get("languageSelect", locale));
        final var msg = format.format(new Object[]{});

        event.reply(msg).addActionRow(menuLanguage).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("language", "Sets the used language of Alpagotchi for this server")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Setzt die verwendete Sprache von Alpagotchi für diesen Server")
                       .setGuildOnly(true)
                       .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }
}