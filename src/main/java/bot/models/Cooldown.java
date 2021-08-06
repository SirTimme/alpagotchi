package bot.models;

import java.util.concurrent.TimeUnit;

public class Cooldown {
    private long sleep, work;

    public Cooldown() {
        sleep = 0L;
        work = 0L;
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
}
