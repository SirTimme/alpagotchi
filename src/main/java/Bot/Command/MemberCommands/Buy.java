package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Shop.Item;
import Bot.Utils.*;
import Bot.Database.IDatabase;
import Bot.Shop.ItemManager;
import Bot.Utils.Error;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;

public class Buy implements ICommand {
	private final ItemManager itemManager;

	public Buy(ItemManager itemManager) {
		this.itemManager = itemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final List<String> args = ctx.getArgs();
		final String prefix = ctx.getPrefix();
		final TextChannel channel = ctx.getChannel();

		if (IDatabase.INSTANCE.getUser(authorID) == null) {
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		if (args.isEmpty() || args.size() < 2) {
			channel.sendMessage(Error.MISSING_ARGS.getMessage(prefix, getName())).queue();
			return;
		}

		final Item item = itemManager.getItem(args.get(0));
		if (item == null) {
			channel.sendMessage(Emote.REDCROSS + " Couldn't resolve the item").queue();
			return;
		}

		try {
			final int amount = Integer.parseInt(args.get(1));
			if (amount > 10) {
				channel.sendMessage(Emote.REDCROSS + " You can purchase max. 10 items at a time").queue();
				return;
			}

			final int price = item.getPrice() * amount;
			final int balance = IDatabase.INSTANCE.getStatInt(authorID, Stat.CURRENCY);
			if (balance - price < 0) {
				channel.sendMessage(Emote.REDCROSS + " Insufficient amount of fluffies").queue();
				return;
			}

			IDatabase.INSTANCE.setStatInt(authorID, Stat.CURRENCY, -price);
			IDatabase.INSTANCE.setInventory(authorID, item, amount);

			channel.sendMessage(":moneybag: You successfully bought **" + Language.handle(amount, item.getName()) + "** for **" + price + "** fluffies").queue();
		}
		catch (NumberFormatException error) {
			channel.sendMessage(Error.NaN.getMessage(prefix, getName())).queue();
		}
	}

	@Override
	public String getName() {
		return "buy";
	}

	@Override
	public PermLevel getPermLevel() {
		return PermLevel.MEMBER;
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}

	@Override
	public String getSyntax() {
		return "buy [item] [1-10]";
	}

	@Override
	public String getExample() {
		return "buy salad 3";
	}

	@Override
	public String getDescription() {
		return "Buys a specific amount of an item from the shop";
	}
}
