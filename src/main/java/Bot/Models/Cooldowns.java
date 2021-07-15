package Bot.Models;

public class Cooldowns {
    private long sleep, work;

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

    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    public void setWork(long work) {
        this.work = work;
    }
}
