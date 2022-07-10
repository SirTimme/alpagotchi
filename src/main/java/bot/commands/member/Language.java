package bot.commands.member;

import bot.commands.InfoCommand;
import bot.components.menus.MenuLanguage;
import bot.components.menus.MenuManager;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.UUID;

public class Language extends InfoCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        final var menuLanguage = SelectMenu.create(UUID.randomUUID().toString())
                                           .setPlaceholder("Available languages")
                                           .addOption("English", "en", Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8"))
                                           .addOption("German", "de", Emoji.fromUnicode("\uD83C\uDDE9\uD83C\uDDEA"))
                                           .build();

        MenuManager.addMenu(menuLanguage.getId(), new MenuLanguage());

        final var format = new MessageFormat(Responses.get("selectLanguage", locale));
        final var msg = format.format(new Object[]{});

        event.reply(msg).addActionRow(menuLanguage).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("language", "Sets the bots language for this server")
                       .setGuildOnly(true)
                       .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }
}