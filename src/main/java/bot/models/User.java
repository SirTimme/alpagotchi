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

    public User(long userId, Alpaca alpaca, Inventory inventory, Cooldown cooldown) {
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

    public void setAlpaca(Alpaca alpaca) {
        this.alpaca = alpaca;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Cooldown getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
    }

    @Transient
    public int getSaturation(String itemType) {
        return itemType.equals("hunger") ? this.getAlpaca().getHunger() : this.getAlpaca().getThirst();
    }

    @Transient
    public void setSaturation(String itemType, int amount) {
        if (itemType.equals("hunger")) {
            this.getAlpaca().setHunger(amount);
        } else {
            this.getAlpaca().setThirst(amount);
        }
    }
}