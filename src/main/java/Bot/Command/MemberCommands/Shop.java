package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermLevel;
import Bot.Config;
import Bot.Shop.ItemManager;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.EnumSet;

public class Shop implements ICommand {
	private final ItemManager itemManager;

	public Shop(ItemManager itemManager) {
		this.itemManager = itemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final User dev = ctx.getJDA().getUserById(Config.get("DEV_ID"));
		final EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle("Shop")
			 .setFooter("Created by " + dev.getName(), dev.getEffectiveAvatarUrl())
			 .addField("__**:meat_on_bone: Hunger items**__", "These items are used to fill up the hunger of your alpaca", false)
			 .setTimestamp(Instant.now());

		itemManager.getSortedItemStream(Stat.HUNGER)
				   .forEach(item -> embed.addField(":package: " + item.getName(), "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(), true));

		embed.addBlankField(false)
			 .addField("__**:beer: Thirst items**__", "Following items replenish the thirst of your alpaca", false);

		itemManager.getSortedItemStream(Stat.THIRST)
				   .forEach(item -> embed.addField(":package: " + item.getName(), "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(), true));

		channel.sendMessage(embed.build()).queue();
	}

	@Override
	public String getName() {
		return "shop";
	}

	@Override
	public PermLevel getPermLevel() {
		return PermLevel.MEMBER;
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
		return EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS);
	}

	@Override
	public String getSyntax() {
		return "shop";
	}

	@Override
	public String getDescription() {
		return "Displays all purchasable items";
	}
}