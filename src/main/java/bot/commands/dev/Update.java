package bot.commands.dev;

import bot.commands.CommandManager;
import bot.commands.ISlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.Responses;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static bot.utils.CommandType.*;

public class Update implements ISlashCommand {
	private final CommandManager commands;

	public Update(final CommandManager commands) {
		this.commands = commands;
	}

	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final Guild guild = event.getGuild();
		if (guild == null) {
			final var format = new MessageFormat(Responses.get("guildOnly", locale));
			final var msg = format.format(new Object[] {});

			event.reply(msg).setEphemeral(true).queue();
			return;
		}

		event.getJDA()
			 .updateCommands()
			 .addCommands(this.commands.getCommandDataByTypes(USER, INFO, INIT))
			 .queue();

		guild.updateCommands()
			 .addCommands(this.commands.getCommandDataByTypes(DEV))
			 .queue(created -> guild.updateCommandPrivileges(createMap(created)).queue());

		final var format = new MessageFormat(Responses.get("update", locale));
		final var msg = format.format(new Object[]{ this.commands.getCommands().size() });

		event.reply(msg).queue();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("update", "Refreshes all slashcommands").setDefaultEnabled(false);
	}

	@Override
	public CommandType getCommandType() {
		return DEV;
	}

	private Map<String, Collection<? extends CommandPrivilege>> createMap(final List<Command> commands) {
		final CommandPrivilege privilege = CommandPrivilege.enableUser(Env.get("DEV_ID"));
		final Function<Command, List<CommandPrivilege>> cmdPrivileges = cmd -> List.of(privilege);

		return commands.stream().collect(Collectors.toMap(Command::getId, cmdPrivileges));
	}
}
