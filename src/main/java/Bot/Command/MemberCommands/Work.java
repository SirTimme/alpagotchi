package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.*;
import Bot.Database.IDatabase;
import Bot.Utils.Error;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Work implements ICommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(Work.class);
	private static final List<String> json = new ArrayList<>();

	public Work() {
		try {
			final File file = new File("src/main/resources/data/work.json");
			final Path filePath = Path.of(file.getPath());
			final String content = Files.readString(filePath);

			final JSONArray messages = new JSONArray(content);

			for (int i = 0; i < messages.length(); i++) {
				json.add(messages.getString(i));
			}
		}
		catch (IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final long authorID = ctx.getAuthorID();
		final String prefix = ctx.getPrefix();

		if (IDatabase.INSTANCE.getUser(authorID) == null) {
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(prefix, getName())).queue();
			return;
		}

		final int energy = IDatabase.INSTANCE.getStatInt(authorID, Stat.ENERGY);
		if (energy < 10) {
			channel.sendMessage("\uD83E\uDD71 Your alpaca is too tired to work, let it rest first with **" + prefix + "sleep**").queue();
			return;
		}

		final int joy = IDatabase.INSTANCE.getStatInt(authorID, Stat.JOY);
		if (joy < 15) {
			channel.sendMessage(":pensive: Your alpaca is too sad to work, give him some love with **" + prefix + "pet**").queue();
			return;
		}

		if (Cooldown.isActive(Stat.WORK, authorID, channel) || Cooldown.isActive(Stat.SLEEP, authorID, channel)) {
			return;
		}

		final int fluffies = (int) (Math.random() * 15 + 1);
		IDatabase.INSTANCE.setStatInt(authorID, Stat.CURRENCY, fluffies);

		final int energyCost = (int) (Math.random() * 8 + 1);
		IDatabase.INSTANCE.setStatInt(authorID, Stat.ENERGY, -energyCost);

		final int joyCost = (int) (Math.random() * 10 + 2);
		IDatabase.INSTANCE.setStatInt(authorID, Stat.JOY, -joyCost);

		final long cooldown = System.currentTimeMillis() + 1000L * 60 * 20;
		IDatabase.INSTANCE.setStatLong(authorID, Stat.WORK, cooldown);

		final String message = getRandomMessage();

		channel.sendMessage("⛏ " + message + " **Fluffies + " + fluffies + ", Energy - " + energyCost + ", Joy - " + joyCost + "**").queue();
	}

	@Override
	public String getName() {
		return "work";
	}

	@Override
	public PermLevel getPermLevel() {
		return PermLevel.MEMBER;
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}

	@Override
	public String getSyntax() {
		return "work";
	}

	@Override
	public String getDescription() {
		return "Work to earn a random amount of fluffies";
	}

	private String getRandomMessage() {
		final int index = (int) (Math.random() * json.size());
		return json.get(index);
	}
}
