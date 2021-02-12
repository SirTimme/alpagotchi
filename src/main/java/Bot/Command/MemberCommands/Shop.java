package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Shop.IShopItem;
import Bot.Config;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.time.Instant;
import java.util.Comparator;

public class Shop implements ICommand {
	private final ShopItemManager shopItemManager;

	public Shop(ShopItemManager shopItemManager) {
		this.shopItemManager = shopItemManager;
	}

	@Override
	public void execute(CommandContext commandContext) {
		final Member botCreator = (Member) commandContext.getJDA().retrieveUserById(Config.get("OWNER_ID"));

		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder
				.setTitle("Shop")
				.addField("Item", getItemsAsString("hunger", "name"), true)
				.addField("Price", getItemsAsString("hunger", "price"), true)
				.addField("Saturation", getItemsAsString("hunger", "saturation"), true)
				.addField("Item", getItemsAsString("thirst", "name"), true)
				.addField("Price", getItemsAsString("thirst", "price"), true)
				.addField("Saturation", getItemsAsString("thirst", "saturation"), true)
				.setFooter("Created by " + botCreator.getEffectiveName(), botCreator.getUser().getEffectiveAvatarUrl())
				.setTimestamp(Instant.now());

		commandContext.getChannel().sendMessage(embedBuilder.build()).queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "shop\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Shows the shop to buy things for your alpaca";
	}

	@Override
	public String getName() {
		return "shop";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	private String getItemsAsString(String category, String filter) {
		StringBuilder stringBuilder = new StringBuilder();

		this.shopItemManager.getShopItems()
				.stream()
				.sorted(Comparator.comparingInt(IShopItem::getPrice))
				.filter((item) -> item.getCategory().equals(category))
				.map(IShopItem::getName)
				.forEach((item) -> {
					if (filter.equals("name")) {
						stringBuilder.append(":package: ").append(item).append("\n");
					} else if (filter.equals("price")) {
						stringBuilder.append(":coin: ").append(this.shopItemManager.getShopItem(item).getPrice()).append("\n");
					} else if (filter.equals("saturation") && category.equals("hunger")) {
						stringBuilder.append(":meat_on_bone: ").append(this.shopItemManager.getShopItem(item).getSaturation()).append("\n");
					} else {
						stringBuilder.append(":beer: ").append(this.shopItemManager.getShopItem(item).getSaturation()).append("\n");
					}
				});

		return stringBuilder.toString();
	}
}
