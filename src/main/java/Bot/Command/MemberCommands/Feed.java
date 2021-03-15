package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDatabase;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Feed implements ICommand {
	private final ShopItemManager shopItemManager;

	public Feed(ShopItemManager shopItemManager) {
		this.shopItemManager = shopItemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();
		final long authorID = ctx.getAuthorID();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		final long sleepCooldown = IDatabase.INSTANCE.getCooldown(authorID, "sleep") - System.currentTimeMillis();
		if (sleepCooldown > 0) {
			int remaining = (int) TimeUnit.MILLISECONDS.toMinutes(sleepCooldown);
			channel.sendMessage("<:RedCross:782229279312314368> Your alpaca sleeps, it will wake up in **" + remaining + (remaining == 1 ? "** minute" : "** minutes")).queue();
			return;
		}

		if (args.isEmpty() || args.size() < 2) {
			channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
			return;
		}

		final IShopItem item = shopItemManager.getShopItem(args.get(0));
		if (item == null) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the item").queue();
			return;
		}

		try {
			final int amount = Integer.parseInt(args.get(1));
			if (amount > 5) {
				channel.sendMessage("<:RedCross:782229279312314368> You can only feed max. 5 items at a time").queue();
				return;
			}

			final String category = item.getCategory();
			final String name = item.getName();

			if (IDatabase.INSTANCE.getInventory(authorID, category, name) - amount < 0) {
				channel.sendMessage("<:RedCross:782229279312314368> You don't own that many items").queue();
				return;
			}

			final int oldValue = IDatabase.INSTANCE.getAlpacaValues(authorID, category);
			final int saturation = item.getSaturation() * amount;

			if (oldValue + saturation > 100) {
				channel.sendMessage("<:RedCross:782229279312314368> You would overfeed your alpaca").queue();
				return;
			}

			IDatabase.INSTANCE.setInventory(authorID, category, name, -amount);
			IDatabase.INSTANCE.setAlpacaValues(authorID, category, saturation);

			final String itemMsg = amount + (amount == 1 ? "** " + name : "** " + name + "s");

			if (item.getCategory().equals("hunger")) {
				channel.sendMessage(":meat_on_bone: Your alpaca eats the **" + itemMsg + " in one bite and is happy **Hunger + " + saturation + "**").queue();
			} else {
				channel.sendMessage(":beer: Your alpaca drinks the **" + itemMsg + " empty **Thirst + " + saturation + "**").queue();
			}
		} catch (NumberFormatException error) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the item amount").queue();
		}
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "feed [itemName] [1-5]\n**Aliases:** " + getAliases() + "\n**Example:** " + prefix + "feed water 2";
	}

	@Override
	public String getName() {
		return "feed";
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