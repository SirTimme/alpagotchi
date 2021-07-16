package Bot.Models;

public class Cooldown {
    private long sleep, work;

    public Cooldown() {
        this.sleep = 0;
        this.work = 0;
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
