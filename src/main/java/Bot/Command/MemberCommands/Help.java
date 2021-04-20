package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Utils.Emote;
import Bot.Utils.PermLevel;
import Bot.Config;
import Bot.Command.CommandManager;
import Bot.Command.ICommand;
import Bot.Database.IDatabase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

public class Help implements ICommand {
	private final CommandManager cmdManager;

	public Help(CommandManager manager) {
		this.cmdManager = manager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final String prefix = IDatabase.INSTANCE.getPrefix(ctx.getGuild().getIdLong());
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();

		if (args.isEmpty()) {
			final User dev = ctx.getJDA().getUserById(Config.get("DEV_ID"));
			final EmbedBuilder embed = new EmbedBuilder();

			embed.setTitle("Overview of all commands")
				 .setDescription("Further information to any command:\n**```fix\n" + prefix + "help [command]\n```**")
				 .addField("Admin commands", getCommandsByLevel(prefix, PermLevel.ADMIN), true)
				 .addField("Member commands", getCommandsByLevel(prefix, PermLevel.MEMBER), true)
				 .addField(
					 "Need further help or found a bug?",
					 "Join the [Alpagotchi Support](https://discord.gg/DXtYyzGhXR) server!",
					 false
				 )
				 .setFooter("Created by " + dev.getName(), dev.getEffectiveAvatarUrl())
				 .setTimestamp(Instant.now());

			channel.sendMessage(embed.build()).queue();
			return;
		}

		final ICommand cmd = cmdManager.getCommand(args.get(0).toLowerCase());
		if (cmd == null) {
			channel.sendMessage(Emote.REDCROSS + " That command doesn't exists").queue();
			return;
		}

		final EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle("Help for " + cmd.getName())
			 .setDescription("[] = required parameters\n() = optional parameters")
			 .addField("Description", cmd.getDescription(), false)
			 .addField("Usage", prefix + cmd.getSyntax(), false)
			 .addField("Example", prefix + cmd.getExample(), false)
			 .addField("Aliases", cmd.getAliases().toString(), false);

		channel.sendMessage(embed.build()).queue();
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public PermLevel getPermLevel() {
		return PermLevel.MEMBER;
	}

	@Override
	public List<String> getAliases() {
		return List.of("commands");
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
		return EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS);
	}

	@Override
	public String getSyntax() {
		return "help (command)";
	}

	@Override
	public String getExample() {
		return "help gift";
	}

	@Override
	public String getDescription() {
		return "Displays all available commands or help for a specific command";
	}

	private String getCommandsByLevel(String prefix, PermLevel permLevel) {
		StringBuilder builder = new StringBuilder();

		this.cmdManager.getCommands()
					   .stream()
					   .filter((cmd) -> cmd.getPermLevel() == permLevel)
					   .map(ICommand::getName)
					   .sorted()
					   .forEach((cmd) -> builder.append("`").append(prefix).append(cmd).append("`\n"));

		return builder.toString();
	}
}

