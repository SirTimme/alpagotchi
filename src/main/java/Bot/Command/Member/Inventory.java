package Bot.Command.Member;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Error;
import Bot.Config;
import Bot.Database.IDatabase;
import Bot.Shop.ItemManager;
import Bot.Utils.Level;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

public class Inventory implements ICommand {
	private final ItemManager itemManager;

	public Inventory(ItemManager itemManager) {
		this.itemManager = itemManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final long authorID = ctx.getAuthorID();

		if (IDatabase.INSTANCE.getUser(authorID) == null) {
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		final User dev = ctx.getJDA().getUserById(Config.get("DEV_ID"));
		final EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle("Inventory")
			 .addField("__**:meat_on_bone: Hunger items**__", "These items are used to fill up the hunger of your alpaca", false)
			 .setFooter("Created by " + dev.getName(), dev.getEffectiveAvatarUrl())
			 .setTimestamp(Instant.now());

		itemManager.getItems(Stat.HUNGER)
				   .forEach(item -> embed.addField(":package: " + item.getName(), "Quantity: **" + IDatabase.INSTANCE.getInventory(authorID, item) + "**", true));

		embed.addBlankField(false)
			 .addField("__**:beer: Thirst items**__", "Following items replenish the thirst of your alpaca", false);

		itemManager.getItems(Stat.THIRST)
				   .forEach(item -> embed.addField(":package: " + item.getName(), "Quantity: **" + IDatabase.INSTANCE.getInventory(authorID, item) + "**", true));

		channel.sendMessage(embed.build()).queue();
	}

	@Override
	public String getName() {
		return "inventory";
	}

	@Override
	public Level getLevel() {
		return Level.MEMBER;
	}

	@Override
	public List<String> getAliases() {
		return List.of("inv");
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
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
}

