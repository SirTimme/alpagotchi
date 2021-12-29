package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.concurrent.TimeUnit;

public class Cooldown {
    private long sleep;
    private long work;

    @BsonCreator
    public Cooldown(@BsonProperty(value = "sleep") final long sleep,
                    @BsonProperty(value = "work") final long work
    ) {
        this.sleep = sleep;
        this.work = work;
    }

    public long getSleep() {
        return TimeUnit.MILLISECONDS.toMinutes(this.sleep - System.currentTimeMillis());
    }

    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    public long getWork() {
        return TimeUnit.MILLISECONDS.toMinutes(this.work - System.currentTimeMillis());
    }

    public void setWork(long work) {
        this.work = work;
    }
}
