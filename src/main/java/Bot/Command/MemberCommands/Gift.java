package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Utils.Emote;
import Bot.Utils.Error;
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
		final String prefix = ctx.getPrefix();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(prefix, getName())).queue();
			return;
		}

		if (args.isEmpty() || args.size() < 3) {
			channel.sendMessage(Error.MISSING_ARGS.getMessage(prefix, getName())).queue();
			return;
		}

		final List<User> users = ctx.getMessage().getMentionedUsers();
		if (users.isEmpty()) {
			channel.sendMessage(Error.MISSING_ARGS.getMessage(prefix, getName())).queue();
			return;
		}

		final User user = users.get(0);
		final long userID = user.getIdLong();
		if (userID == authorID) {
			channel.sendMessage(Emote.REDCROSS + " You cannot gift yourself items").queue();
			return;
		}

		if (!IDatabase.INSTANCE.isUserInDB(userID)) {
			channel.sendMessage(Emote.REDCROSS + " The mentioned user doesn't own an alpaca, he's to use **" + prefix + "init** first").queue();
			return;
		}

		IShopItem item = shopItemManager.getShopItem(ctx.getArgs().get(1));
		if (item == null) {
			channel.sendMessage(Emote.REDCROSS + " This item doesn't exists").queue();
			return;
		}

		try {
			final int amount = Integer.parseInt(ctx.getArgs().get(2));
			if (amount > 5) {
				channel.sendMessage(Emote.REDCROSS + " You can gift max. 5 items at a time").queue();
				return;
			}

			if (IDatabase.INSTANCE.getInventory(authorID, item) - amount < 0) {
				channel.sendMessage(Emote.REDCROSS + " You don't own that many items to gift").queue();
				return;
			}

			IDatabase.INSTANCE.setInventory(authorID, item, -amount);
			IDatabase.INSTANCE.setInventory(userID, item, amount);

			channel.sendMessage("\uD83C\uDF81 You successfully gifted **" + amount + " " + item.getName() + "** to **" + user.getName() + "**").queue();
		}
		catch (NumberFormatException error) {
			channel.sendMessage(Error.NaN.getMessage(prefix, getName())).queue();
		}
	}

	@Override
	public String getName() {
		return "gift";
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
		return "gift [@user] [item] [1-5]";
	}

	@Override
	public String getExample() {
		return "gift <@" + Config.get("BOT_ID") + "> taco 3";
	}

	@Override
	public String getDescription() {
		return "Gifts the mentioned user the specific items";
	}
}
