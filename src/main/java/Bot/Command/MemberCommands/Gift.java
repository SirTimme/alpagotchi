package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDatabase;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.EnumSet;
import java.util.List;

public class Gift implements ICommand {
	private final ShopItemManager shopItemManager;

	public Gift(ShopItemManager shopItemManager) {
		this.shopItemManager = shopItemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();
		final long authorID = ctx.getAuthorID();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage("<:RedCross:782229279312314368> You don't own an alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		if (args.isEmpty() || args.size() < 3) {
			channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
			return;
		}

		final List<User> mentionedUsers = ctx.getMessage().getMentionedUsers();
		if (mentionedUsers.isEmpty()) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the mentioned user").queue();
			return;
		}

		final long giftedUserID = mentionedUsers.get(0).getIdLong();
		if (giftedUserID == authorID) {
			channel.sendMessage("<:RedCross:782229279312314368> You cannot gift yourself items").queue();
			return;
		}

		if (!IDatabase.INSTANCE.isUserInDB(giftedUserID)) {
			channel.sendMessage("<:RedCross:782229279312314368> The mentioned user doesn't own an alpaca, he's to use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		IShopItem item = shopItemManager.getShopItem(ctx.getArgs().get(1));
		if (item == null) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the specified item").queue();
			return;
		}

		try {
			final int amount = Integer.parseInt(ctx.getArgs().get(2));
			if (amount > 5) {
				channel.sendMessage("<:RedCross:782229279312314368> You can gift max. 5 items at a time").queue();
				return;
			}

			final String name = item.getName();
			final String category = item.getCategory();

			if (IDatabase.INSTANCE.getInventory(authorID, category, name) - amount < 0) {
				channel.sendMessage("<:RedCross:782229279312314368> You don't own that many items to gift").queue();
				return;
			}

			IDatabase.INSTANCE.setInventory(authorID, category, name, -amount);
			IDatabase.INSTANCE.setInventory(giftedUserID, category, name, amount);

			final String username = mentionedUsers.get(0).getName();

			channel.sendMessage("\uD83C\uDF81 You successfully gifted **" + amount + " " + name + "** to **" + username + "**").queue();
		} catch (NumberFormatException error) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the amount of items").queue();
		}
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "gift [@user] [item] [1-5]\n**Aliases:** " + getAliases() + "\n**Example:** " + prefix + "gift <@" + Config.get("BOT_ID") + "> taco 3";
	}

	@Override
	public String getName() {
		return "gift";
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
