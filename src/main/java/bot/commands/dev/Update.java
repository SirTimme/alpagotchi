package bot.commands.dev;

import bot.commands.interfaces.IDevCommand;
import bot.commands.SlashCommandManager;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.text.MessageFormat;

public class Update implements IDevCommand {
	private final SlashCommandManager manager;

	public Update(SlashCommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void execute(SlashCommandEvent event) {
		final Guild guild = event.getGuild();
		if (guild == null) {
			MessageService.reply(event, new MessageFormat(Responses.get("guildOnly")), true);
			return;
		}

		manager.getCommands().forEach(cmd -> {
			if (cmd.getCommandType() == CommandType.DEV) {
				guild.upsertCommand(cmd.getCommandData()).queue();
			}
			else {
				event.getJDA().upsertCommand(cmd.getCommandData()).queue();
			}
		});

		guild.retrieveCommands()
			 .queue(commands ->
					commands.forEach(cmd ->
							cmd.updatePrivileges(guild, CommandPrivilege.enableUser(Env.get("DEV_ID"))).queue()
					)
			 );

		final MessageFormat msg = new MessageFormat(Responses.get("update"));
		final String content = msg.format(new Object[]{ manager.getCommands().size() });

		MessageService.reply(event, content, false);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("update", "Refreshes all slashcommands").setDefaultEnabled(false);
	}
}
