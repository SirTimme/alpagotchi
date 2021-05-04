package Bot.Command;

import Bot.Command.Admin.SetBalance;
import Bot.Command.Admin.SetPrefix;
import Bot.Command.Dev.Decrease;
import Bot.Command.Dev.Shutdown;
import Bot.Command.Member.*;
import Bot.Dresses.DressManager;
import Bot.Shop.ItemManager;
import Bot.Utils.Emote;
import Bot.Utils.Level;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
public class CommandManager {
	private List<ICommand> commands = new ArrayList<>();

	public CommandManager(EventWaiter waiter) {
		ItemManager itemManager = new ItemManager();
		DressManager dressManager = new DressManager();

		addCommand(new MyAlpaca());
		addCommand(new Help(this));
		addCommand(new SetPrefix());
		addCommand(new Balance());
		addCommand(new Work());
		addCommand(new Shop(itemManager));
		addCommand(new Buy(itemManager));
		addCommand(new Inventory(itemManager));
		addCommand(new Feed(itemManager));
		addCommand(new Decrease());
		addCommand(new Nick());
		addCommand(new Pet());
		addCommand(new Gift(itemManager));
		addCommand(new Shutdown());
		addCommand(new Sleep());
		addCommand(new SetBalance());
		addCommand(new Init(waiter));
		addCommand(new Outfit(dressManager));
		addCommand(new Delete(waiter));
		addCommand(new Ping());
		addCommand(new Count());
		addCommand(new Image());

		commands = commands.stream().sorted().collect(Collectors.toList());
	}

	private void addCommand(ICommand command) {
		this.commands.add(command);
	}

	private List<ICommand> getCommands(Level level) {
		return commands.stream().filter(cmd -> cmd.getLevel().equals(level)).collect(Collectors.toList());
	}

	public String getCommandsString(String prefix, Level level, boolean part) {
		StringBuilder sb = new StringBuilder();
		int index = commands.size() / 2;

		Map<Boolean, List<ICommand>> splitList = getCommands(level).stream().collect(Collectors.partitioningBy(cmd -> commands.indexOf(cmd) < index));
		splitList.get(part).forEach(cmd -> sb.append("`").append(prefix).append(cmd.getName()).append("`\n"));

		return sb.toString();
	}

	@Nullable
	public ICommand getCommand(String search) {
		for (ICommand cmd : this.commands) {
			if (cmd.getName().equals(search) || cmd.getAliases().contains(search)) {
				return cmd;
			}
		}
		return null;
	}

	public void handle(GuildMessageReceivedEvent event, String prefix) {
		final String[] cmdArgs = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(prefix), "").split("\\s+");
		final ICommand cmd = getCommand(cmdArgs[0].toLowerCase());

		if (cmd == null || !permsValid(cmd, event)) {
			return;
		}

		if (!cmd.getLevel().hasPermission(event.getMember())) {
			final TextChannel channel = event.getChannel();

			switch (cmd.getLevel()) {
				case DEVELOPER:
					channel.sendMessage(Emote.REDCROSS + " This is an **admin-only** command, you're missing the **Manage Server** permission").queue();
					break;
				case ADMIN:
					channel.sendMessage(Emote.REDCROSS + " This is a **dev-only** command").queue();
					break;
			}
			return;
		}

		final List<String> args = Arrays.asList(cmdArgs).subList(1, cmdArgs.length);
		final long authorID = event.getMember().getIdLong();

		cmd.execute(new CommandContext(event, args, authorID, prefix));
	}

	private boolean permsValid(ICommand command, GuildMessageReceivedEvent event) {
		Member botClient = event.getGuild().getSelfMember();
		TextChannel channel = event.getChannel();

		EnumSet<Permission> requiredPermissions = command.getCommandPerms();
		EnumSet<Permission> actualPermissions = botClient.getPermissions(channel);

		requiredPermissions.removeAll(actualPermissions);

		if (requiredPermissions.size() > 0) {
			Permission permission = requiredPermissions.iterator().next();
			if (permission != Permission.MESSAGE_WRITE) {
				channel.sendMessage(":warning: Im missing at least the **" + permission.getName() + "** permission to execute this command").queue();
			}
			return false;
		}
		return true;
	}
}
