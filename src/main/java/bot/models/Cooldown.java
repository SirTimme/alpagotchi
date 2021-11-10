package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

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
