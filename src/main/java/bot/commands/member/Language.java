package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.components.menus.MenuLanguage;
import bot.components.menus.MenuManager;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.UUID;

public class Language implements ISlashCommand {
	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final Guild guild = event.getGuild();
		if (guild == null) {
			MessageService.queueReply(event, new MessageFormat(Responses.get("guildOnly", locale)), true);
			return;
		}

		final long ownerID = guild.getOwnerIdLong();
		if (event.getUser().getIdLong() != ownerID) {
			MessageService.queueReply(event, new MessageFormat(Responses.get("userNotOwner", locale)), true);
			return;
		}

		final SelectionMenu menuLanguage = SelectionMenu.create(UUID.randomUUID().toString())
				.setPlaceholder("Available languages")
				.addOption("English", "en", Emoji.fromMarkdown("\uD83C\uDDFA\uD83C\uDDF8"))
				.addOption("German", "de", Emoji.fromMarkdown("\uD83C\uDDE9\uD83C\uDDEA"))
				.build();

		MenuManager.addMenu(menuLanguage.getId(), new MenuLanguage());

		MessageService.queueComponentReply(event, new MessageFormat(Responses.get("selectLanguage", locale)), true, menuLanguage);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("language", "Sets the bots language for this server");
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.INFO;
	}
}
