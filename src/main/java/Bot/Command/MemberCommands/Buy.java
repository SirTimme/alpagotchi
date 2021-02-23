package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Shop.IShopItem;
import Bot.Database.IDataBaseManager;
import Bot.Shop.ShopItemManager;

public class Buy implements ICommand {
	private final ShopItemManager shopItemManager;

	public Buy(ShopItemManager shopItemManager) {
		this.shopItemManager = shopItemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		final IShopItem item;
		try {
			item = shopItemManager.getShopItem(ctx.getArgs().get(0));
		} catch (IndexOutOfBoundsException error) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve an item with this name").queue();
			return;
		}

		if (item == null) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve an item with this name").queue();
			return;
		}

		final int itemAmount;
		try {
			itemAmount = Integer.parseInt(ctx.getArgs().get(1));
		} catch (NumberFormatException | IndexOutOfBoundsException error) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the amount of items").queue();
			return;
		}

		if (itemAmount > 10) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You can purchase max. 10 items at a time").queue();
			return;
		}

		if (IDataBaseManager.INSTANCE.getBalance(ctx.getAuthorID()) - (item.getPrice() * itemAmount) < 0) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Insufficient amount of fluffies").queue();
			return;
		}

		final int price = item.getPrice() * itemAmount;
		IDataBaseManager.INSTANCE.setBalance(ctx.getAuthorID(), -price);
		IDataBaseManager.INSTANCE.setInventory(ctx.getAuthorID(), item.getCategory(), item.getName(), itemAmount);

		ctx.getChannel().sendMessage(":moneybag: Congratulations, you successfully bought **" + itemAmount + " " + item.getName() + "** for **" + (item.getPrice() * itemAmount) + "** fluffies").queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "buy [itemName] [1-10]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Buys the specified amount of a item from the shop";
	}

	@Override
	public String getName() {
		return "buy";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}
}
