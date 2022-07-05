package bot.commands.dev;

import bot.commands.ISlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.text.MessageFormat;
import java.util.Locale;

public class Shutdown implements ISlashCommand {
	@Override
	public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
		final var format = new MessageFormat(Responses.get("shutdown", locale));
		final var msg = format.format(new Object[]{ event.getJDA().getSelfUser().getName() });

		event.reply(msg).complete();
		event.getJDA().shutdown();
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("shutdown", "Shutdowns Alpagotchi")
					   .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.DEV;
	}
}