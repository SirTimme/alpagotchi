package Bot.Utils;

import Bot.Database.IDatabase;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public class Cooldown {
	public static boolean isActive(Activity activity, long authorID, TextChannel channel) {
		final long cooldown = IDatabase.INSTANCE.getCooldown(authorID, activity.toString().toLowerCase()) - System.currentTimeMillis();

		if (cooldown > 0) {
			final long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldown);

			switch (activity) {
				case WORK:
					channel.sendMessage(Emote.REDCROSS + " Your alpaca sleeps, it will wake up in **" + Language.handle(minutes, "minute") + "**").queue();
					break;
				case SLEEP:
					channel.sendMessage(Emote.REDCROSS + " Your alpaca has to rest **" + Language.handle(minutes, "minute") + "** to work again").queue();
					break;
			}
			return true;
		}
		return false;
	}
}
