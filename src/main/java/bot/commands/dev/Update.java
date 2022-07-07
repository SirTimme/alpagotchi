package bot.commands.dev;

import bot.commands.CommandManager;
import bot.commands.ISlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.Responses;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.text.MessageFormat;
import java.util.*;

import static bot.utils.CommandType.*;

public class Update implements ISlashCommand {
	private final CommandManager commands;

	public Update(final CommandManager commands) {
		this.commands = commands;
	}

	@Override
	public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
		final Guild guild = event.getJDA().getGuildById(Env.get("DEV_GUILD"));
		if (guild == null) {
			final var format = new MessageFormat(Responses.get("guildOnly", locale));
			final var msg = format.format(new Object());

			event.reply(msg).setEphemeral(true).queue();
			return;
		}

		event.getJDA()
			 .updateCommands()
			 .addCommands(this.commands.getCommandDataByTypes(USER, INFO, INIT))
			 .queue();

		guild.updateCommands()
			 .addCommands(this.commands.getCommandDataByTypes(DEV))
			 .queue();

		final var format = new MessageFormat(Responses.get("update", locale));
		final var msg = format.format(new Object[]{ this.commands.getCommands().size() });

		event.reply(msg).queue();
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("update", "Refreshes all slashcommands")
					   .setGuildOnly(true)
					   .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
	}

	@Override
	public CommandType getCommandType() {
		return DEV;
	}
}