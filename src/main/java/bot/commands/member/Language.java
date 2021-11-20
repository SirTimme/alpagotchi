package bot.commands.member;

import bot.commands.interfaces.IDevCommand;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import static bot.utils.Emote.REDCROSS;

public class Language implements IDevCommand {
	@Override
	public void execute(SlashCommandEvent event) {
		final long ownerID = event.getGuild().getOwnerIdLong();
		if (event.getUser().getIdLong() != ownerID) {
			event.reply(REDCROSS + " Only the guild owner can change the bots language").setEphemeral(true).queue();
			return;
		}

		final SelectionMenu menu = SelectionMenu.create("menu:language")
				.setPlaceholder("Available languages")
				.addOption("English", "lang_english", Emoji.fromMarkdown("\uD83C\uDDFA\uD83C\uDDF8"))
				.addOption("German", "lang_german", Emoji.fromMarkdown("\uD83C\uDDE9\uD83C\uDDEA"))
				.build();

		event.reply("Please select the language you want Alpagotchi to use on this server:")
			 .setEphemeral(true)
			 .addActionRow(menu)
			 .queue();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("language", "Sets the bots language for this server");
	}
}
