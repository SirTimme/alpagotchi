package bot.models;

import jakarta.persistence.*;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "cooldowns")
public class Cooldown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long sleep;
    private long work;

    @BsonCreator
    public Cooldown(final long sleep, final long work) {
        this.sleep = sleep;
        this.work = work;
    }

    @Transient
    public long getSleepMinutes() {
        return TimeUnit.MILLISECONDS.toMinutes(this.sleep - System.currentTimeMillis());
    }

    public long getSleep() {
        return this.sleep;
    }

    public void setSleep(final long sleep) {
        this.sleep = sleep;
    }

    @Transient
    public long getWorkMinutes() {
        return TimeUnit.MILLISECONDS.toMinutes(this.work - System.currentTimeMillis());
    }

    public long getWork() {
        return this.work;
    }

    public void setWork(final long work) {
        this.work = work;
    }
}