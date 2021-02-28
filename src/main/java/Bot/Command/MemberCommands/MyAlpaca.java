package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import Bot.Outfits.IOutfit;
import Bot.Outfits.OutfitManager;
import Bot.Utils.ImagePreloader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class MyAlpaca implements ICommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyAlpaca.class);
	private final OutfitManager outfitManager;

	public MyAlpaca(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}

	@Override
	public void execute(CommandContext ctx) {
		if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		final IOutfit outfit = outfitManager.getOutfit(IDataBaseManager.INSTANCE.getOutfit(ctx.getAuthorID()));
		final BufferedImage alpaca = ImagePreloader.getAlpacaImage(outfit.getName());

		final Graphics graphics = alpaca.getGraphics();
		graphics.setFont(new Font("SansSerif", Font.BOLD, 15));

		final int hunger = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), "hunger");
		final int thirst = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), "thirst");
		final int energy = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), "energy");
		final int joy = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), "joy");

		graphics.setColor(Color.BLACK);
		graphics.drawString(hunger + "/100", getPosition(hunger, "front"), 24);
		graphics.drawString(thirst + "/100", getPosition(thirst, "front"), 66);
		graphics.drawString(energy + "/100", getPosition(energy, "back"), 24);
		graphics.drawString(joy + "/100", getPosition(joy, "back"), 66);

		graphics.setColor(getColorOfValues(hunger));
		graphics.fillRect(31, 31, (int) (hunger * 1.75), 12);

		graphics.setColor(getColorOfValues(thirst));
		graphics.fillRect(31, 73, (int) (thirst * 1.75), 12);

		graphics.setColor(getColorOfValues(energy));
		graphics.fillRect(420, 31, (int) (energy * 1.75), 12);

		graphics.setColor(getColorOfValues(joy));
		graphics.fillRect(420, 73, (int) (joy * 1.75), 12);

		final File newAlpacaFile = new File("src/main/resources/alpacaEdited.jpg");
		try {
			ImageIO.write(alpaca, "jpg", newAlpacaFile);
		} catch (IOException error) {
			LOGGER.error(error.getMessage());
		}

		final User botCreator = ctx.getJDA().getUserById(Config.get("OWNER_ID"));
		final EmbedBuilder embed = new EmbedBuilder();
		embed
				.setTitle("" + IDataBaseManager.INSTANCE.getNickname(ctx.getAuthorID()) + "")
				.setDescription("_Have a llamazing day!_")
				.setThumbnail(ctx.getMember().getUser().getAvatarUrl())
				.setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
				.setTimestamp(Instant.now())
				.setImage("attachment://alpacaEdited.jpg");

		ctx.getChannel().sendFile(newAlpacaFile, "alpacaEdited.jpg").embed(embed.build()).queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "myalpaca\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Shows the stats of your alpaca";
	}

	@Override
	public String getName() {
		return "myalpaca";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public List<String> getAliases() {
		return List.of("ma", "stats");
	}

	private Color getColorOfValues(int value) {
		if (value >= 80) {
			return Color.GREEN;
		} else if (value >= 60) {
			return Color.YELLOW;
		} else if (value >= 40) {
			return Color.ORANGE;
		} else if (value >= 20) {
			return Color.RED;
		} else {
			return Color.BLACK;
		}
	}

	private int getPosition(int value, String position) {
		if (value == 100) {
			return position.equalsIgnoreCase("front") ? 145 : 534;
		} else if (value >= 10) {
			return position.equalsIgnoreCase("front") ? 155 : 544;
		} else {
			return position.equalsIgnoreCase("front") ? 165 : 554;
		}
	}
}
