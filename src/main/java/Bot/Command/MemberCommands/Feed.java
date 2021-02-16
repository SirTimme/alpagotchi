package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Feed implements ICommand {
	private final ShopItemManager shopItemManager;

	public Feed(ShopItemManager shopItemManager) {
		this.shopItemManager = shopItemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		long sleepCooldown = IDataBaseManager.INSTANCE.getCooldown(ctx.getAuthorID(), "sleep") - System.currentTimeMillis();
		int remainingMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(sleepCooldown);

		if (sleepCooldown > 0) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca sleeps, it will wake up in **" + (remainingMinutes == 1 ? remainingMinutes + "** minute" : remainingMinutes + "** minutes")).queue();
			return;
		}

		final List<String> args = ctx.getArgs();

		if (args.isEmpty() || args.size() < 2) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
			return;
		}

		IShopItem item = shopItemManager.getShopItem(args.get(0));

		if (item == null) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve this item").queue();
			return;
		}

		int itemAmount;

		try {
			itemAmount = Integer.parseInt(args.get(1));

		} catch (NumberFormatException error) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the amount of item").queue();
			return;
		}

		if (itemAmount > 5) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You can only feed max. 5 items at a time").queue();
			return;
		}

		if (IDataBaseManager.INSTANCE.getInventory(ctx.getAuthorID(), item.getCategory(), item.getName()) - itemAmount < 0) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own that many items").queue();
			return;
		}

		int oldValue = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), item.getCategory());

		int saturation = item.getCategory().equalsIgnoreCase("hunger") ? item.getSaturation() * itemAmount : item.getSaturation() * itemAmount;

		if (oldValue + saturation > 100) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You would overfeed your alpaca").queue();
			return;
		}

		IDataBaseManager.INSTANCE.setInventory(ctx.getAuthorID(), item.getCategory(), item.getName(), -itemAmount);
		IDataBaseManager.INSTANCE.setAlpacaValues(ctx.getAuthorID(), item.getCategory(), saturation);

		String itemMessage = itemAmount == 1 ? itemAmount + "** " + item.getName() : itemAmount + "** " + item.getName() + "s";

		if (item.getCategory().equals("hunger")) {
			ctx.getChannel().sendMessage(":meat_on_bone: Your alpaca eats the **" + itemMessage + " in one bite and is happy **Hunger + " + saturation + "**").queue();

		} else {
			ctx.getChannel().sendMessage(":beer: Your alpaca drinks the **" + itemMessage + " empty **Thirst + " + saturation + "**").queue();
		}
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "feed [itemName] [1-5]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Feeds your alpaca with the specified item";
	}

	@Override
	public String getName() {
		return "feed";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}
}