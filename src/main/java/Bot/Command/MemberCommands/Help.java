package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Utils.PermissionLevel;
import Bot.Config;
import Bot.Command.CommandManager;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.time.Instant;
import java.util.List;

public class Help implements ICommand {
	private final CommandManager cmdManager;

	public Help(CommandManager manager) {
		this.cmdManager = manager;
	}

	@Override
	public void execute(CommandContext ctx) throws PermissionException {
		final String prefix = IDataBaseManager.INSTANCE.getPrefix(ctx.getGuild().getIdLong());

		if (ctx.getArgs().isEmpty()) {
			final User botCreator = ctx.getJDA().getUserById(Config.get("OWNER_ID"));
			final EmbedBuilder embed = new EmbedBuilder();
			embed
					.setTitle("Overview of all commands")
					.setDescription("Further information to any command:\n**```fix\n" + prefix + "help [command]\n```**")
					.addField("Admin commands", getCommandsByPerms(prefix, PermissionLevel.ADMIN), true)
					.addField("Member commands", getCommandsByPerms(prefix, PermissionLevel.MEMBER), true)
					.addField("Need further help or found a bug?", "Then join the [Alpagotchi Support](https://discord.gg/SErfVpSQAV) server!", false)
					.setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
					.setTimestamp(Instant.now());

			ctx.getChannel().sendMessage(embed.build()).queue();
			return;
		}

		final ICommand cmd = cmdManager.getCommand(ctx.getArgs().get(0).toLowerCase());
		if (cmd == null) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not retrieve help for that command").queue();
			return;
		}

		ctx.getChannel().sendMessage(cmd.getHelp(prefix)).queue();
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

