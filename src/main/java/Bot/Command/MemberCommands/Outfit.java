package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Dresses.Dress;
import Bot.Utils.Emote;
import Bot.Utils.Error;
import Bot.Utils.PermLevel;
import Bot.Config;
import Bot.Database.IDatabase;
import Bot.Dresses.DressManager;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

public class Outfit implements ICommand {
	private final DressManager dressManager;

	public Outfit(DressManager dressManager) {
		this.dressManager = dressManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();

		if (IDatabase.INSTANCE.getUser(authorID) == null) {
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		if (args.isEmpty()) {
			final User dev = ctx.getJDA().getUserById(Config.get("DEV_ID"));
			final EmbedBuilder embed = new EmbedBuilder();

			for (Dress outfit : dressManager.getOutfits()) {
				embed.addField("\uD83D\uDC54 " + outfit.getName(), outfit.getDescription(), false);
			}

			embed.setTitle("Available outfits")
				 .setFooter("Created by " + dev.getName(), dev.getEffectiveAvatarUrl())
				 .setTimestamp(Instant.now());

			channel.sendMessage(embed.build()).queue();
			return;
		}

		Dress outfit = dressManager.getOutfit(args.get(0).toLowerCase());
		if (outfit == null) {
			channel.sendMessage(Emote.REDCROSS + " Couldn't resolve the outfit").queue();
			return;
		}

		final String outfitName = outfit.getName();
		IDatabase.INSTANCE.setStatString(authorID, Stat.OUTFIT , outfitName);

		channel.sendMessage("\uD83D\uDC54 The outfit of your alpaca has been set to **" + outfitName + "**").queue();
	}

	@Override
	public String getName() {
		return "outfit";
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
		return "outfit (outfit)";
	}

	@Override
	public String getExample() {
		return "outfit king";
	}

	@Override
	public String getDescription() {
		return "Gives your alpaca an outfit";
	}
}
