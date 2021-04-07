package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.*;
import Bot.Database.IDatabase;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;
import Bot.Utils.Error;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;

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
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		if (Cooldown.isActive(Activity.SLEEP, authorID, channel)) {
			return;
		}

		if (args.isEmpty() || args.size() < 2) {
			channel.sendMessage(Error.MISSING_ARGS.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		final IShopItem item = shopItemManager.getShopItem(args.get(0));
		if (item == null) {
			channel.sendMessage(Emote.REDCROSS + "Couldn't resolve the item").queue();
			return;
		}

		try {
			final int amount = Integer.parseInt(args.get(1));
			if (amount > 5) {
				channel.sendMessage(Emote.REDCROSS + " You can only feed max. 5 items at a time").queue();
				return;
			}

			if (IDatabase.INSTANCE.getInventory(authorID, item) - amount < 0) {
				channel.sendMessage(Emote.REDCROSS + " You don't own that many items").queue();
				return;
			}

			final int oldValue = IDatabase.INSTANCE.getStat(authorID, item.getStat());
			final int saturation = item.getSaturation() * amount;

			if (oldValue + saturation > 100) {
				channel.sendMessage(Emote.REDCROSS + " You would overfeed your alpaca").queue();
				return;
			}

			IDatabase.INSTANCE.setInventory(authorID, item, -amount);
			IDatabase.INSTANCE.setStat(authorID, item.getStat(), saturation);

			if (item.getStat().equals(Stat.HUNGER)) {
				channel.sendMessage(":meat_on_bone: Your alpaca eats the **" + Language.handle(amount, item.getName()) + "** in one bite **Hunger + " + saturation + "**").queue();
			}
			else {
				channel.sendMessage(":beer: Your alpaca drinks the **" + Language.handle(amount, item.getName()) + "** empty **Thirst + " + saturation + "**").queue();
			}
		}
		catch (NumberFormatException error) {
			channel.sendMessage(Emote.REDCROSS + " Couldn't resolve the item amount").queue();
		}
	}

	@Override
	public String getName() {
		return "feed";
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}

	@Override
	public String getSyntax() {
		return "feed [item] [1-5]";
	}

	@Override
	public String getExample() {
		return "feed water 2";
	}

	@Override
	public String getDescription() {
		return "Feeds your alpaca with the specified item";
	}
}