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

public class Shop implements ICommand {
	private final ShopItemManager shopItemManager;

	public Shop(ShopItemManager shopItemManager) {
		this.shopItemManager = shopItemManager;
	}

	@Override
	public void execute(CommandContext commandContext) {
		final Member botCreator = commandContext.getGuild().getMemberById(Config.get("OWNER_ID"));

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
		String emoji = filter.equals("name") ? ":package:" : filter.equals("price") ? ":coin:" : filter.equals("saturation") && category.equals("hunger") ? ":meat_on_bone:" : ":beer:";

		this.shopItemManager.getShopItems()
				.stream()
				.filter((item) -> item.getCategory().equals(category))
				.map(filter.equals("name") ? IShopItem::getName : filter.equals("price") ? IShopItem::getPrice : IShopItem::getSaturation)
				.sorted()
				.forEach((item) -> stringBuilder.append(emoji).append(" ").append(item).append("\n"));

		return stringBuilder.toString();
	}
}
