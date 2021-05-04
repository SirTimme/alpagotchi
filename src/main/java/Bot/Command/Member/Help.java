package Bot.Command.Member;

import Bot.Command.CommandContext;
import Bot.Utils.Emote;
import Bot.Config;
import Bot.Command.CommandManager;
import Bot.Command.ICommand;
import Bot.Database.IDatabase;
import Bot.Utils.Level;
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
		final User dev = ctx.getJDA().getUserById(Config.get("DEV_ID"));

		if (args.isEmpty()) {
			final EmbedBuilder embed = new EmbedBuilder();

			embed.setTitle("Overview of all commands")
				 .setDescription("Further information to any command:\n**```fix\n" + prefix + "help (command)\n```**")
				 .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/836542447186214942/avatar.png")
				 .setImage("https://cdn.discordapp.com/attachments/795637300661977132/836986469607276554/Help.PNG")
				 .addField("Admin commands", cmdManager.getCommandsString(prefix, Level.ADMIN, false), true)
				 .addField("", cmdManager.getCommandsString(prefix, Level.ADMIN, true), true)
				 .addBlankField(true)
				 .addField("Member commands", cmdManager.getCommandsString(prefix, Level.MEMBER, true), true)
				 .addField("", cmdManager.getCommandsString(prefix, Level.MEMBER, false), true)
				 .addBlankField(true)
				 .addField("Need further help or found a bug?","Join the [Alpagotchi Support](https://discord.gg/DXtYyzGhXR) server!",false)
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

		embed.setTitle("Help")
			 .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/836542447186214942/avatar.png")
			 .setImage("https://cdn.discordapp.com/attachments/795637300661977132/836986469607276554/Help.PNG")
			 .addField("Description", cmd.getDescription(), false)
			 .addField("Aliases", cmd.getAliases().isEmpty() ? "none" : cmd.getAliases().toString(), false)
			 .addField("Usage", prefix + cmd.getSyntax(), false)
			 .addField("Example", prefix + cmd.getExample(), false)
			 .addField("Need further help or found a bug?","Join the [Alpagotchi Support](https://discord.gg/DXtYyzGhXR) server!",false)
			 .setFooter("Created by " + dev.getName(), dev.getEffectiveAvatarUrl())
			 .setTimestamp(Instant.now());

		channel.sendMessage(embed.build()).queue();
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public Level getLevel() {
		return Level.MEMBER;
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
}

