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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("ConstantConditions")
public class CommandManager {
	private final List<ICommand> commands = new ArrayList<>();
	private final static Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
	ShopItemManager shopItemManager = new ShopItemManager();
	OutfitManager outfitManager = new OutfitManager();

	public CommandManager(EventWaiter waiter) {
		addCommand(new MyAlpaca());
		addCommand(new Help(this));
		addCommand(new SetPrefix());
		addCommand(new Balance());
		addCommand(new Work());
		addCommand(new Shop(this.shopItemManager));
		addCommand(new Buy(this.shopItemManager));
		addCommand(new Inventory(this.shopItemManager));
		addCommand(new Feed(this.shopItemManager));
		addCommand(new Decrease());
		addCommand(new Nick());
		addCommand(new Pet());
		addCommand(new Gift(this.shopItemManager));
		addCommand(new Shutdown());
		addCommand(new Sleep());
		addCommand(new SetBalance());
		addCommand(new Init(waiter));
		addCommand(new Outfit(this.outfitManager));
		addCommand(new Delete(waiter));
		addCommand(new Ping());
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
		String[] cmdArgs = event.getMessage()
								.getContentRaw()
								.replaceFirst("(?i)" + Pattern.quote(prefix), "")
								.split("\\s+");

		ICommand command = getCommand(cmdArgs[0].toLowerCase());

		if (command == null || !checkPermissions(command, event)) {
			return;
		}

		List<String> args = Arrays.asList(cmdArgs).subList(1, cmdArgs.length);
		long authorID = event.getMember().getIdLong();

		command.execute(new CommandContext(event, args, authorID, prefix));
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
				channel.sendMessage(":warning: Im missing at least the **" + permission.getName() + "** permission " +
					"to execute this command")
					   .queue();
			}
			return false;
		}
		return true;
	}
}
