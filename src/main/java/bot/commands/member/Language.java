package bot.commands.member;

import bot.commands.types.InfoCommand;
import bot.utils.CommandType;
import bot.localization.LocalizedResponse;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.Locale;

public class Language extends InfoCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        final var userId = event.getUser().getId();

        final var menuLanguage = StringSelectMenu
                .create(userId + ":language")
                .setPlaceholder(LocalizedResponse.get("language.placeholder", locale))
                .addOption(LocalizedResponse.get("general.displayName.english", locale), "en", Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8"))
                .addOption(LocalizedResponse.get("general.displayName.german", locale), "de", Emoji.fromUnicode("\uD83C\uDDE9\uD83C\uDDEA"))
                .build();

        event.reply(LocalizedResponse.get("language.select", locale)).addActionRow(menuLanguage).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("language", "Sets the used language of Alpagotchi for this server")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Setzt die verwendete Sprache von Alpagotchi für diesen Server")
                       .setGuildOnly(true)
                       .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER));
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.ADMIN;
    }
}