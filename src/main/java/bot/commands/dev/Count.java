package bot.commands.dev;

import bot.commands.ISlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.text.MessageFormat;
import java.util.Locale;

public class Count implements ISlashCommand {
	@Override
	public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
		final var msg = new MessageFormat(Responses.get("count", locale));
		final var content = msg.format(new Object[]{ IDatabase.INSTANCE.getEntries(), event.getJDA().getGuilds().size() });

		event.reply(content).queue();
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("count", "Counts all alpacas of Alpagotchi")
					   .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.DEV;
	}
}