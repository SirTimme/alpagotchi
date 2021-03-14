package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Config;
import Bot.Database.IDatabase;
import Bot.Outfits.IOutfit;
import Bot.Outfits.OutfitManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

public class Outfit implements ICommand {
	private final OutfitManager outfitManager;

	public Outfit(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage("<:RedCross:782229279312314368> You don't own an alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		if (args.isEmpty()) {
			final User botCreator = ctx.getJDA().getUserById(Config.get("DEV_ID"));
			final EmbedBuilder embed = new EmbedBuilder();

			for (IOutfit outfit : outfitManager.getOutfits()) {
				embed.addField("\uD83D\uDC54 " + outfit.getName(), outfit.getDescription(), false);
			}

			embed.setTitle("Available outfits")
				 .setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
				 .setTimestamp(Instant.now());

			channel.sendMessage(embed.build()).queue();
			return;
		}

		IOutfit outfit = outfitManager.getOutfit(args.get(0).toLowerCase());
		if (outfit == null) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the outfit").queue();
			return;
		}

		final String name = outfit.getName();
		IDatabase.INSTANCE.setOutfit(authorID, name);

		channel.sendMessage("\uD83D\uDC54 The outfit of your alpaca has been set to **" + name + "**").queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "outfit (outfit)\n**Aliases:** " + getAliases() + "\n**Example:** " + prefix + "outfit pirate";
	}

	@Override
	public String getName() {
		return "outfit";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS);
	}
}
