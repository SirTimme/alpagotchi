package Bot.Models;

public class Cooldowns {
    private final long sleep, work;

    public Cooldowns(long sleep, long work) {
        this.sleep = sleep;
        this.work = work;
    }

    public long getSleep() {
        return sleep;
    }

    public long getWork() {
        return work;
    }
}
