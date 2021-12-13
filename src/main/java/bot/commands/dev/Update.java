package bot.commands.dev;

import bot.commands.interfaces.IDevCommand;
import bot.commands.CommandManager;
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
import java.util.stream.Collectors;

import static bot.utils.CommandType.*;

public class Update implements IDevCommand {
	private final CommandManager commands;

	public Update(CommandManager commands) {
		this.commands = commands;
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("update", "Refreshes all slashcommands").setDefaultEnabled(false);
	}

	@Override
	public void execute(final SlashCommandEvent event, final Locale locale) {
		final Guild guild = event.getGuild();
		if (guild == null) {
			MessageService.reply(event, new MessageFormat(Responses.get("guildOnly", locale)), true);
			return;
		}

		event.getJDA()
			 .updateCommands()
			 .addCommands(this.commands.getCommandDataByType(STATIC_USER, INFO, DYNAMIC_USER))
			 .queue();

		guild.updateCommands()
			 .addCommands(this.commands.getCommandDataByType(DEV))
			 .queue(created -> guild.updateCommandPrivileges(createMap(created)).queue());

		final MessageFormat msg = new MessageFormat(Responses.get("update", locale));
		final String content = msg.format(new Object[]{ commands.getCommands().size() });

		MessageService.reply(event, content, false);
	}

	private Map<String, Collection<? extends CommandPrivilege>> createMap(final List<Command> commands) {
		return commands.stream().collect(Collectors.toMap(Command::getId, x -> List.of(CommandPrivilege.enableUser(Env.get("DEV_ID")))));
	}
}
