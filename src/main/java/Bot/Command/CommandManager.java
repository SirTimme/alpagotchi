package Bot.Command;

import Bot.Command.AdminCommands.SetBalance;
import Bot.Command.DeveloperCommands.Decrease;
import Bot.Command.AdminCommands.SetPrefix;
import Bot.Command.DeveloperCommands.Shutdown;
import Bot.Command.MemberCommands.*;
import Bot.Outfits.OutfitManager;
import Bot.Shop.ShopItemManager;
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
		ShopItemManager shopItemManager = new ShopItemManager();
		OutfitManager outfitManager = new OutfitManager();

		addCommand(new MyAlpaca());
		addCommand(new Help(this));
		addCommand(new SetPrefix());
		addCommand(new Balance());
		addCommand(new Work());
		addCommand(new Shop(shopItemManager));
		addCommand(new Buy(shopItemManager));
		addCommand(new Inventory(shopItemManager));
		addCommand(new Feed(shopItemManager));
		addCommand(new Decrease());
		addCommand(new Nick());
		addCommand(new Pet());
		addCommand(new Gift(shopItemManager));
		addCommand(new Shutdown());
		addCommand(new Sleep());
		addCommand(new SetBalance());
		addCommand(new Init(waiter));
		addCommand(new Outfit(outfitManager));
		addCommand(new Delete(waiter));
		addCommand(new Ping());
		addCommand(new Count());
	}

	private void addCommand(ICommand command) {
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

		if (cmd == null || !checkPermissions(cmd, event)) {
			return;
		}

		final List<String> args = Arrays.asList(cmdArgs).subList(1, cmdArgs.length);
		final long authorID = event.getMember().getIdLong();

		cmd.execute(new CommandContext(event, args, authorID, prefix));
	}

	private boolean checkPermissions(ICommand command, GuildMessageReceivedEvent event) {
		Member botClient = event.getGuild().getSelfMember();
		TextChannel channel = event.getChannel();

		EnumSet<Permission> requiredPermissions = command.getRequiredPermissions();
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
