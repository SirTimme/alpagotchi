package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Shop.IShopItem;
import Bot.Database.IDatabase;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;

public class Buy implements ICommand {
	private final ShopItemManager shopItemManager;

	public Buy(ShopItemManager shopItemManager) {
		this.shopItemManager = shopItemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final List<String> args = ctx.getArgs();
		final String prefix = ctx.getPrefix();
		final TextChannel channel = ctx.getChannel();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage("<:RedCross:782229279312314368> You don't own an alpaca, " +
				"use **" + prefix + "init** first")
				   .queue();
			return;
		}

		if (args.isEmpty() || args.size() < 2) {
			channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
			return;
		}

		final IShopItem item = shopItemManager.getShopItem(args.get(0));
		if (item == null) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the shop item").queue();
			return;
		}

		try {
			final int amount = Integer.parseInt(args.get(1));
			if (amount > 10) {
				channel.sendMessage("<:RedCross:782229279312314368> You can purchase max. 10 items at a time").queue();
				return;
			}

			final int price = item.getPrice() * amount;
			if (IDatabase.INSTANCE.getBalance(authorID) - price < 0) {
				channel.sendMessage("<:RedCross:782229279312314368> Insufficient amount of fluffies").queue();
				return;
			}

			final String name = item.getName();

			IDatabase.INSTANCE.setBalance(authorID, -price);
			IDatabase.INSTANCE.setInventory(authorID, item.getCategory(), name, amount);

			channel.sendMessage(":moneybag: You successfully bought **" + amount + " " + name + "** " +
				"for **" + price + "** fluffies")
				   .queue();
		}
		catch (NumberFormatException error) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the item amount").queue();
		}
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "buy [item] [1-10]\n" +
			"**Aliases:** " + getAliases() + "\n" +
			"**Example:** " + prefix + "buy salad 3";
	}

	@Override
	public String getName() {
		return "buy";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}
}
