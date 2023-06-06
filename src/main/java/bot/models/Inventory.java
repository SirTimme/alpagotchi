package bot.models;

import jakarta.persistence.*;
import java.util.Map;

@Entity
@Table(name = "inventories")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "currency", nullable = false)
    private int currency = 0;

    @ElementCollection
    @CollectionTable(name = "item_map", joinColumns = @JoinColumn(name = "item_map_id"))
    @MapKeyColumn(name = "item")
    @Column(name = "amount")
    private Map<String, Integer> items;

    public Inventory() { }

    public int getCurrency() {
        return this.currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public Map<String, Integer> getItems() {
        return this.items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    @Transient
    public int getAmount(String itemName) {
        return this.items.get(itemName);
    }

    @Transient
    public void setItem(String itemName, int amount) {
        this.items.replace(itemName, amount);
    }
}