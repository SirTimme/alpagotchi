package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Config;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.Comparator;
import java.util.EnumSet;

public class Shop implements ICommand {
	private final ShopItemManager shopItemManager;

	public Shop(ShopItemManager shopItemManager) {
		this.shopItemManager = shopItemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final User botCreator = ctx.getJDA().getUserById(Config.get("DEV_ID"));
		final EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle("Shop")
			 .addField("Item", getItemsAsString("hunger", "name"), true)
			 .addField("Price", getItemsAsString("hunger", "price"), true)
			 .addField("Saturation", getItemsAsString("hunger", "saturation"), true)
			 .addField("Item", getItemsAsString("thirst", "name"), true)
			 .addField("Price", getItemsAsString("thirst", "price"), true)
			 .addField("Saturation", getItemsAsString("thirst", "saturation"), true)
			 .setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
			 .setTimestamp(Instant.now());

		channel.sendMessage(embed.build()).queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "shop\n**Aliases:** " + getAliases() + "\n**Example:** " + prefix + "shop";
	}

	@Override
	public String getName() {
		return "shop";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS);
	}

	private String getItemsAsString(String category, String filter) {
		StringBuilder builder = new StringBuilder();

		this.shopItemManager.getShopItems()
							.stream()
							.sorted(Comparator.comparingInt(IShopItem::getPrice))
							.filter((item) -> item.getCategory().equals(category))
							.map(IShopItem::getName)
							.forEach((item) -> {
								if (filter.equals("name")) {
									builder.append(":package: ").append(item).append("\n");
								} else if (filter.equals("price")) {
									builder.append(":coin: ").append(this.shopItemManager.getShopItem(item).getPrice()).append("\n");
								} else if (filter.equals("saturation") && category.equals("hunger")) {
									builder.append(":meat_on_bone: ").append(this.shopItemManager.getShopItem(item).getSaturation()).append("\n");
								} else {
									builder.append(":beer: ").append(this.shopItemManager.getShopItem(item).getSaturation()).append("\n");
								}
							});

		return builder.toString();
	}
}