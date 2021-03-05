package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import Bot.Outfits.IOutfit;
import Bot.Outfits.OutfitManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.time.Instant;

public class Outfit implements ICommand {
	private final OutfitManager outfitManager;

	public Outfit(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}

	@Override
	public void execute(CommandContext ctx) throws PermissionException {
		if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		if (ctx.getArgs().isEmpty()) {
			final User botCreator = ctx.getJDA().getUserById(Config.get("OWNER_ID"));
			final EmbedBuilder embed = new EmbedBuilder();

			for (IOutfit outfit : outfitManager.getOutfits()) {
				embed.addField("\uD83D\uDC54 " + outfit.getName(), outfit.getDescription(), false);
			}

			embed.setTitle("Available outfits")
					.setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
					.setTimestamp(Instant.now());

			ctx.getChannel().sendMessage(embed.build()).queue();
			return;
		}

		IOutfit chosenOutfit = outfitManager.getOutfit(ctx.getArgs().get(0).toLowerCase());
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
