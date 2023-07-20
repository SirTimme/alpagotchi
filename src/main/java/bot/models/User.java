package bot.models;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "users", indexes = @Index(name = "idx_user_id", unique = true, columnList = "user_id"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NaturalId
    @Column(name = "user_id", nullable = false)
    private long userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "alpaca_id")
    private Alpaca alpaca;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cooldown_id")
    private Cooldown cooldown;

    public User() { }

    public User(final long userId, final Alpaca alpaca, final Inventory inventory, final Cooldown cooldown) {
        this.userId = userId;
        this.alpaca = alpaca;
        this.inventory = inventory;
        this.cooldown = cooldown;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }

    public Alpaca getAlpaca() {
        return this.alpaca;
    }

    public void setAlpaca(final Alpaca alpaca) {
        this.alpaca = alpaca;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(final Inventory inventory) {
        this.inventory = inventory;
    }

    public Cooldown getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(final Cooldown cooldown) {
        this.cooldown = cooldown;
    }
}