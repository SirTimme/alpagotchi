package bot.models;

public class Cooldown {
    private long sleep, work;

    public Cooldown(long sleep, long work) {
        this.sleep = sleep;
        this.work = work;
    }

    public long getSleep() {
        return this.sleep;
    }

    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    public long getWork() {
        return this.work;
    }

    public void setWork(long work) {
        this.work = work;
    }
}
