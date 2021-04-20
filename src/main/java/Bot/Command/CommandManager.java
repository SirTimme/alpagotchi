package Bot.Command;

import Bot.Command.AdminCommands.SetBalance;
import Bot.Command.AdminCommands.SetPrefix;
import Bot.Command.DevCommands.Decrease;
import Bot.Command.DevCommands.Shutdown;
import Bot.Command.MemberCommands.*;
import Bot.Dresses.DressManager;
import Bot.Shop.ItemManager;
import Bot.Utils.Emote;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("ConstantConditions")
public class CommandManager {
	private final List<ICommand> commands = new ArrayList<>();

	public CommandManager(EventWaiter waiter) {
		ItemManager itemManager = new ItemManager();
		DressManager dressManager = new DressManager();

		add(new MyAlpaca());
		add(new Help(this));
		add(new SetPrefix());
		add(new Balance());
		add(new Work());
		add(new Shop(itemManager));
		add(new Buy(itemManager));
		add(new Inventory(itemManager));
		add(new Feed(itemManager));
		add(new Decrease());
		add(new Nick());
		add(new Pet());
		add(new Gift(itemManager));
		add(new Shutdown());
		add(new Sleep());
		add(new SetBalance());
		add(new Init(waiter));
		add(new Outfit(dressManager));
		add(new Delete(waiter));
		add(new Ping());
		add(new Count());
	}

	private void add(ICommand command) {
		this.commands.add(command);
	}

	public List<ICommand> getCommands() {
		return this.commands;
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
		final String[] cmdArgs = event.getMessage()
									  .getContentRaw()
									  .replaceFirst("(?i)" + Pattern.quote(prefix), "")
									  .split("\\s+");

		final ICommand cmd = getCommand(cmdArgs[0].toLowerCase());

		if (cmd == null || !permsValid(cmd, event)) {
			return;
		}

		if (!cmd.getPermLevel().hasPermission(event.getMember())) {
			final TextChannel channel = event.getChannel();

			switch (cmd.getPermLevel()) {
				case DEVELOPER -> channel.sendMessage(Emote.REDCROSS + " This is an **admin-only** command, you're missing the **Manage Server** permission").queue();
				case ADMIN -> channel.sendMessage(Emote.REDCROSS + " This is a **dev-only** command").queue();
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
