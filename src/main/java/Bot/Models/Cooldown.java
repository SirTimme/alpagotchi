package Bot.Models;

import java.util.concurrent.TimeUnit;

public class Cooldown {
    private long sleep, work, daily;
    private int streak;

    public Cooldown() {
        sleep = 0L;
        work = 0L;
        daily = 0L;
        streak = 0;
    }

    public long getSleep() {
        return TimeUnit.MILLISECONDS.toMinutes(sleep - System.currentTimeMillis());
    }

    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    public long getWork() {
        return TimeUnit.MILLISECONDS.toMinutes(work - System.currentTimeMillis());
    }

    public void setWork(long work) {
        this.work = work;
    }

    public long getDaily() {
        return TimeUnit.MILLISECONDS.toMinutes(daily - System.currentTimeMillis());
    }

    public void setDaily(long daily) {
        this.daily = daily;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak += streak;
    }
}
