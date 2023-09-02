package bot.models.cooldown;

import java.util.concurrent.TimeUnit;

public class CooldownUtils {
    public static long toMinutes(final long cooldown) {
        return TimeUnit.MILLISECONDS.toMinutes(cooldown - System.currentTimeMillis());
    }

    public static long setCooldown(final int minutes) {
        return System.currentTimeMillis() + 1000L * 60 * minutes;
    }
}