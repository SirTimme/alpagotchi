package Bot.Command;

import Bot.Command.AdminCommands.SetBalance;
import Bot.Command.DeveloperCommands.Decrease;
import Bot.Command.AdminCommands.SetPrefix;
import Bot.Command.DeveloperCommands.Shutdown;
import Bot.Command.MemberCommands.*;
import Bot.Outfits.OutfitManager;
import Bot.Shop.ShopItemManager;
import Bot.Utils.PermissionManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
	private final List<ICommand> commands = new ArrayList<>();
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
		String[] split = event.getMessage()
				.getContentRaw()
				.replaceFirst("(?i)" + Pattern.quote(prefix), "")
				.split("\\s+");

		ICommand command = getCommand(split[0].toLowerCase());
		if (command == null) {
			return;
		}

		List<String> args = Arrays.asList(split).subList(1, split.length);
		long authorID = event.getMember().getIdLong();

		try {
			command.execute(new CommandContext(event, args, authorID, prefix));
		} catch (PermissionException error) {
			String permission = error.getMessage().split(":")[1].trim();
			Permission missingPermission = error.getPermission() == Permission.UNKNOWN ? PermissionManager.getPermission(permission) : error.getPermission();

			if (missingPermission != Permission.MESSAGE_WRITE) {
				event.getChannel().sendMessage("⚠ I am missing at least the **" + missingPermission.getName() + "** permission to execute this command").queue();
			}
		}
	}
}
