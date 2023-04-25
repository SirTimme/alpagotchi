package bot.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users", indexes = @Index(name = "idx_user_id", unique = true, columnList = "user_id"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "alpaca_id")
    private Alpaca alpaca;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    public User() {

    }

    public User(long userId, Alpaca alpaca, Inventory inventory) {
        this.userId = userId;
        this.alpaca = alpaca;
        this.inventory = inventory;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }
}