package bot.commands.dev;

import bot.commands.interfaces.IDevCommand;
import bot.commands.SlashCommandManager;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.Resources;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.text.MessageFormat;

import static bot.utils.Emote.REDCROSS;

public class Update implements IDevCommand {
	private final SlashCommandManager manager;

	public Update(SlashCommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void execute(SlashCommandEvent event) {
		final Guild guild = event.getGuild();
		if (guild == null) {
			event.reply(REDCROSS + " You need to execute this command in a guild!")
				 .setEphemeral(true)
				 .queue();
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
			 .queue(commands -> {
				 commands.forEach(cmd -> {
					 cmd.updatePrivileges(guild, CommandPrivilege.enableUser(Env.get("DEV_ID"))).queue();
				 });
			 });

		final MessageFormat msg = new MessageFormat(Resources.getPattern("update"));
		event.reply(msg.format(new Object[]{ manager.getCommands().size() })).queue();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("update", "Refreshes all slashcommands").setDefaultEnabled(false);
	}
}
