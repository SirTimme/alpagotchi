package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Config;
import Bot.Database.IDatabase;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

public class Inventory implements ICommand {
	private final ShopItemManager itemManager;

	public Inventory(ShopItemManager itemManager) {
		this.itemManager = itemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final long authorID = ctx.getAuthorID();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage("<:RedCross:782229279312314368> You don't own an alpaca, " +
				"use **" + ctx.getPrefix() + "init** first")
				   .queue();
			return;
		}

		final User botCreator = ctx.getJDA().getUserById(Config.get("DEV_ID"));
		final EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle("Inventory")
			 .addField("Hunger", getItemsByCategory("hunger", authorID), true)
			 .addField("Thirst", getItemsByCategory("thirst", authorID), true)
			 .setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
			 .setTimestamp(Instant.now());

		channel.sendMessage(embed.build()).queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "inventory\n*+Aliases:** " + getAliases() + "\n**Example:** " + prefix + "inventory";
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

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS);
	}

	private String getItemsByCategory(String category, long memberID) {
		StringBuilder builder = new StringBuilder();
		String emoji = category.equals("hunger")
					   ? ":meat_on_bone:"
					   : ":beer:";

		this.itemManager.getShopItems()
						.stream()
						.filter((item) -> item.getCategory().equals(category))
						.map(IShopItem::getName)
						.sorted()
						.forEach((item) -> {
							int itemAmount = IDatabase.INSTANCE.getInventory(memberID, category, item);

							builder.append(emoji)
								   .append(" **")
								   .append(itemAmount)
								   .append("** ")
								   .append(item)
								   .append("\n");
						});

		return builder.toString();
	}
}

