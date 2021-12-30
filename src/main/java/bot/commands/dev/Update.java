package bot.commands.dev;

import bot.commands.CommandManager;
import bot.commands.ISlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.MessageService;
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

	public Update(CommandManager commands) {
		this.commands = commands;
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("update", "Refreshes all slashcommands").setDefaultEnabled(false);
	}

	@Override
	public CommandType getCommandType() {
		return DEV;
	}

	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final Guild guild = event.getGuild();
		if (guild == null) {
			MessageService.queueReply(event, new MessageFormat(Responses.get("guildOnly", locale)), true);
			return;
		}

		event.getJDA()
			 .updateCommands()
			 .addCommands(this.commands.getCommandDataByTypes(USER, INFO, INIT))
			 .queue();

		guild.updateCommands()
			 .addCommands(this.commands.getCommandDataByTypes(DEV))
			 .queue(created -> guild.updateCommandPrivileges(createMap(created)).queue());

		final MessageFormat msg = new MessageFormat(Responses.get("update", locale));
		final String content = msg.format(new Object[]{ this.commands.getCommands().size() });

		MessageService.queueReply(event, content, false);
	}

	private Map<String, Collection<? extends CommandPrivilege>> createMap(final List<Command> commands) {
		final CommandPrivilege privilege = CommandPrivilege.enableUser(Env.get("DEV_ID"));
		final Function<Command, List<CommandPrivilege>> cmdPrivileges = cmd -> List.of(privilege);

		return commands.stream().collect(Collectors.toMap(Command::getId, cmdPrivileges));
	}
}
