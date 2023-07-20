package bot.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cooldowns")
public class Cooldown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "sleep", nullable = false)
    private long sleep = 0L;

    @Column(name = "work", nullable = false)
    private long work = 0L;

    public Cooldown() { }

    public long getSleep() {
        return this.sleep;
    }

    public void setSleep(final long sleep) {
        this.sleep = sleep;
    }

    public long getWork() {
        return this.work;
    }

    public void setWork(final long work) {
        this.work = work;
    }
}