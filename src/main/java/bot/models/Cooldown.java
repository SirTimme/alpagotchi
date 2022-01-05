package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
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

    @BsonIgnore
    public long getSleepMins() {
        return TimeUnit.MILLISECONDS.toMinutes(this.sleep - System.currentTimeMillis());
    }

    public long getSleep() {
        return this.sleep;
    }

    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    @BsonIgnore
    public long getWorkMins() {
        return TimeUnit.MILLISECONDS.toMinutes(this.work - System.currentTimeMillis());
    }

    public long getWork() {
        return this.work;
    }

    public void setWork(long work) {
        this.work = work;
    }
}
