package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Emote;
import Bot.Utils.Error;
import Bot.Utils.PermissionLevel;
import Bot.Config;
import Bot.Database.IDatabase;
import Bot.Shop.ShopItemManager;
import Bot.Utils.Stat;
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
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		final User dev = ctx.getJDA().getUserById(Config.get("DEV_ID"));
		final EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Inventory")
			 .addField("Hunger", getItemsByStat(Stat.HUNGER, authorID), true)
			 .addField("Thirst", getItemsByStat(Stat.THIRST, authorID), true)
			 .setFooter("Created by " + dev.getName(), dev.getEffectiveAvatarUrl())
			 .setTimestamp(Instant.now());

		channel.sendMessage(embed.build()).queue();
	}

	@Override
	public String getName() {
		return "inventory";
	}

	@Override
	public PermissionLevel getPermissionLevel() {
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

	@Override
	public String getSyntax() {
		return "inventory";
	}

	@Override
	public String getDescription() {
		return "Displays your bought items";
	}

	private String getItemsByStat(Stat stat, long memberID) {
		StringBuilder builder = new StringBuilder();
		String emoji = stat.equals(Stat.HUNGER) ? ":meat_on_bone:" : ":beer:";

		itemManager.getShopItems()
				   .stream()
				   .filter((item) -> item.getStat().equals(stat))
				   .forEach((item) -> {
					   final int amount = IDatabase.INSTANCE.getInventory(memberID, item);

					   builder.append(emoji).append(" **").append(amount).append("** ").append(item.getName()).append("\n");
				   });

		return builder.toString();
	}
}

