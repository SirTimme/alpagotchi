package bot.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "alpaca_id", nullable = false)
    private Alpaca alpaca;

    public User() {

    }

    public User(long userId, Alpaca alpaca) {
        this.userId = userId;
        this.alpaca = alpaca;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }
}