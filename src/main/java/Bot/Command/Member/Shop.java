package Bot.Command.Member;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Shop.Item;
import Bot.Shop.ItemManager;
import Bot.Utils.Level;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.Comparator;
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
			 .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/839072735182323732/shop.png")
			 .setFooter("Created by " + dev.getName(), dev.getEffectiveAvatarUrl())
			 .addField("__**:meat_on_bone: Hunger items**__", "These items are used to fill up the hunger of your alpaca", false)
			 .setTimestamp(Instant.now());

		itemManager.getItems(Stat.HUNGER)
				   .stream().sorted(Comparator.comparingInt(Item::getPrice))
				   .forEach(item -> embed.addField(":package: " + item.getName(), "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(), true));

		embed.addBlankField(false)
			 .addField("__**:beer: Thirst items**__", "Following items replenish the thirst of your alpaca", false);

		itemManager.getItems(Stat.THIRST)
				   .stream().sorted(Comparator.comparingInt(Item::getPrice))
				   .forEach(item -> embed.addField(":package: " + item.getName(), "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(), true));

		channel.sendMessage(embed.build()).queue();
	}

	@Override
	public String getName() {
		return "shop";
	}

	@Override
	public Level getLevel() {
		return Level.MEMBER;
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