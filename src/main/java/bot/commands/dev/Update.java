package bot.commands.dev;

import bot.commands.IInfoCommand;
import bot.commands.SlashCommandManager;
import bot.utils.Env;
import bot.utils.Resources;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.text.MessageFormat;
import java.util.Set;

import static bot.utils.Emote.REDCROSS;

public class Update implements IInfoCommand {
	private final SlashCommandManager slashCmdMan;
	private final static Set<String> DEV_COMMANDS = Set.of("shutdown", "update", "count");

	public Update(SlashCommandManager slashCmdMan) {
		this.slashCmdMan = slashCmdMan;
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

		slashCmdMan.getCommands().forEach(cmd -> {
			final CommandData cmdData = cmd.getCommandData();
			if (DEV_COMMANDS.contains(cmdData.getName())) {
				guild.upsertCommand(cmdData).queue();
			}
			else {
				event.getJDA().upsertCommand(cmdData).queue();
			}
		});

		guild.retrieveCommands()
			 .queue(commands -> {
				 commands.forEach(cmd -> {
					 cmd.updatePrivileges(guild, CommandPrivilege.enableUser(Env.get("DEV_ID"))).queue();
				 });
			 });

		final MessageFormat msg = new MessageFormat(Resources.getPattern("update"));
		event.reply(msg.format(new Object[]{ slashCmdMan.getCommands().size() })).queue();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("update", "Refreshes all slashcommands").setDefaultEnabled(false);
	}
}
