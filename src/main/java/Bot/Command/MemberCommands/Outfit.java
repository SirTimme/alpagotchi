package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import Bot.Outfits.IOutfit;
import Bot.Outfits.OutfitManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.time.Instant;
import java.util.List;

public class Outfit implements ICommand {
	private final OutfitManager outfitManager;

	public Outfit(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		List<String> args = ctx.getArgs();

		if (args.isEmpty()) {
			EmbedBuilder embedBuilder = new EmbedBuilder();
			final Member botCreator = (Member) ctx.getJDA().retrieveUserById(Config.get("OWNER_ID"));

			embedBuilder.setTitle("Available outfits");

			for (IOutfit outfit : outfitManager.getOutfits()) {
				embedBuilder.addField("\uD83D\uDC54 " + outfit.getName(), outfit.getDescription(), false);
			}

			embedBuilder.setFooter("Created by " + botCreator.getEffectiveName(), botCreator.getUser().getEffectiveAvatarUrl());
			embedBuilder.setTimestamp(Instant.now());

			ctx.getChannel().sendMessage(embedBuilder.build()).queue();
			return;
		}

		IOutfit chosenOutfit = outfitManager.getOutfit(args.get(0));

		if (chosenOutfit == null) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the specified outfit").queue();
			return;
		}

		IDataBaseManager.INSTANCE.setOutfit(ctx.getAuthorID(), chosenOutfit.getName());

		ctx.getChannel().sendMessage("\uD83D\uDC54 The outfit of your alpaca has been set to **" + chosenOutfit.getName() + "**").queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "outfit [outfit]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Change the appearance of your alpaca, pass no outfit to see all available outfits";
	}

	@Override
	public String getName() {
		return "outfit";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}
}
