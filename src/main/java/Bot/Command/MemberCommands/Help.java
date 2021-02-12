package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.PermissionLevel;
import Bot.Config;
import Bot.Command.CommandManager;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.time.Instant;
import java.util.List;

public class Help implements ICommand {
	private final CommandManager cmdManager;

	public Help(CommandManager manager) {
		this.cmdManager = manager;
	}

	@Override
	public void execute(CommandContext commandContext) {
		final List<String> args = commandContext.getArgs();
		final String prefix = IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong());

		if (args.isEmpty()) {
			final Member botCreator = (Member) commandContext.getJDA().retrieveUserById(Config.get("OWNER_ID"));

			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Overview of all commands")
					.setDescription("Further information to any command:\n**```fix\n" + prefix + "help [command]\n```**")
					.addField("\uD83D\uDC6E Admin commands", getCommandsByPerms(prefix, PermissionLevel.ADMIN), true)
					.addField("\uD83D\uDD13 Member commands", getCommandsByPerms(prefix, PermissionLevel.MEMBER), true)
					.setFooter("Created by " + botCreator.getEffectiveName(), botCreator.getUser().getEffectiveAvatarUrl())
					.setTimestamp(Instant.now());

			commandContext.getChannel().sendMessage(embed.build()).queue();
			return;
		}

		ICommand command = cmdManager.getCommand(args.get(0));

		if (command == null) {
			commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not retrieve help for that command").queue();
			return;
		}

		commandContext.getChannel().sendMessage(command.getHelp(prefix)).queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "help [command]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Displays further information to a specific command";
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public List<String> getAliases() {
		return List.of("commands");
	}

	private String getCommandsByPerms(String prefix, Enum<PermissionLevel> permLevel) {
		StringBuilder stringBuilder = new StringBuilder();

		cmdManager.getCommands()
				.stream()
				.filter((cmd) -> cmd.getPermissionLevel() == permLevel)
				.map(ICommand::getName)
				.sorted()
				.forEach((cmd) -> stringBuilder.append("`").append(prefix).append(cmd).append("`\n"));

		return stringBuilder.toString();
	}
}

