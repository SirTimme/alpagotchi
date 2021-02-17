package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.List;

public class Inventory implements ICommand {
	private final ShopItemManager shopItemManager;

	public Inventory(ShopItemManager shopItemManager) {
		this.shopItemManager = shopItemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final User botCreator = ctx.getJDA().getUserById(Config.get("OWNER_ID"));

		if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Inventory")
				.addField("Hunger", getItemsByCategory("hunger", ctx.getAuthorID()), true)
				.addField("Thirst", getItemsByCategory("thirst", ctx.getAuthorID()), true)
				.setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
				.setTimestamp(Instant.now());

		ctx.getChannel().sendMessage(embed.build()).queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "inventory\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Displays your inventory with the bought items from the shop";
	}

	@Override
	public String getName() {
		return "inventory";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public List<String> getAliases() {
		return List.of("inv");
	}

	private String getItemsByCategory(String category, long memberID) {
		StringBuilder stringBuilder = new StringBuilder();
		String emoji = category.equals("hunger") ? ":meat_on_bone:" : ":beer:";

		this.shopItemManager.getShopItems()
				.stream()
				.filter((item) -> item.getCategory().equals(category))
				.map(IShopItem::getName)
				.sorted()
				.forEach((item) -> stringBuilder.append(emoji).append(" **").append(IDataBaseManager.INSTANCE.getInventory(memberID, category, item)).append("** ").append(item).append("\n"));

		return stringBuilder.toString();
	}
}

