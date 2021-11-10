package bot.commands;

import bot.commands.dev.*;
import bot.commands.dev.Count;
import bot.commands.member.*;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.shop.ItemManager;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.*;

import static bot.utils.Emote.REDCROSS;

public class SlashCommandManager {
	private final static Set<String> INFO_COMMANDS = Set.of("help", "image", "ping", "shop", "count", "shutdown", "update");
	private final static Set<String> STATIC_COMMANDS = Set.of("balance", "delete", "init", "inventory", "myalpaca");
	private final static Set<String> DEV_COMMANDS = Set.of("count", "shutdown", "update");

	private final Map<String, ISlashCommand> commands = new TreeMap<>();

	public SlashCommandManager() {
		final ItemManager items = new ItemManager();

		commands.put("ping", new Ping());
		commands.put("init", new Init());
		commands.put("balance", new Balance());
		commands.put("buy", new Buy(items));
		commands.put("count", new Count());
		commands.put("delete", new Delete());
		commands.put("feed", new Feed(items));
		commands.put("gift", new Gift(items));
		commands.put("image", new Image());
		commands.put("work", new Work());
		commands.put("shutdown", new Shutdown());
		commands.put("myalpaca", new MyAlpaca());
		commands.put("nick", new Nick());
		commands.put("help", new Help(this));
		commands.put("sleep", new Sleep());
		commands.put("outfit", new Outfit());
		commands.put("pet", new Pet());
		commands.put("inventory", new Inventory(items));
		commands.put("shop", new Shop(items));
		commands.put("update", new Update(this));
	}

	public void handle(SlashCommandEvent event) {
		final String eventName = event.getName();

		if (INFO_COMMANDS.contains(eventName)) {
			((IInfoCommand) getCommand(eventName)).execute(event);
		}
		else {
			final Entry user = IDatabase.INSTANCE.getUser(event.getUser().getIdLong());

			if (user == null && !eventName.equals("init")) {
				event.reply(REDCROSS + " You don't own an alpaca, use **/init** first")
					 .setEphemeral(true)
					 .queue();
				return;
			}

			if (STATIC_COMMANDS.contains(eventName)) {
				((IStaticUserCommand) getCommand(eventName)).execute(event, user);
			}
			else {
				final Entry modifiedUser = ((IDynamicUserCommand) getCommand(eventName)).execute(event, user);

				if (modifiedUser != null) {
					IDatabase.INSTANCE.updateUser(modifiedUser);
				}
			}
		}
	}

	public Collection<ISlashCommand> getCommands() {
		return commands.values();
	}

	public String getCommandsString() {
		StringBuilder sb = new StringBuilder();

		commands.keySet()
				.stream()
				.filter(cmd -> !DEV_COMMANDS.contains(cmd))
				.forEach(cmd -> sb.append("`").append(cmd).append("` "));

		return sb.toString();
	}

	private ISlashCommand getCommand(String eventName) {
		return commands.get(eventName);
	}
}
