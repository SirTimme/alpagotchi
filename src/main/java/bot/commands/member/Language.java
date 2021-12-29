package bot.commands.member;

import bot.commands.SlashCommand;
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

public class Language extends SlashCommand {
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

		final SelectionMenu menu = SelectionMenu.create("menu:language")
				.setPlaceholder("Available languages")
				.addOption("English", "lang_english", Emoji.fromMarkdown("\uD83C\uDDFA\uD83C\uDDF8"))
				.addOption("German", "lang_german", Emoji.fromMarkdown("\uD83C\uDDE9\uD83C\uDDEA"))
				.build();

		MessageService.queueReply(event, new MessageFormat(Responses.get("selectLanguage", locale)), true, menu);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("language", "Sets the bots language for this server");
	}

	@Override
	protected CommandType getCommandType() {
		return CommandType.INFO;
	}
}
