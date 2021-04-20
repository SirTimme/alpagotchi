package Bot.Utils;

import Bot.Database.IDatabase;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public class Cooldown {
	public static boolean isActive(Stat stat, long authorID, TextChannel channel) {
		final long cooldown = IDatabase.INSTANCE.getStatLong(authorID, stat) - System.currentTimeMillis();

		if (cooldown > 0) {
			final long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldown);

			switch (stat) {
				case SLEEP:
					channel.sendMessage(Emote.REDCROSS + " Your alpaca sleeps, it will wake up in **" + Language.handle(minutes, "minute") + "**").queue();
					break;
				case WORK:
					channel.sendMessage(Emote.REDCROSS + " Your alpaca has to rest **" + Language.handle(minutes, "minute") + "** to work again").queue();
					break;
			}
			return true;
		}
		return false;
	}
}
