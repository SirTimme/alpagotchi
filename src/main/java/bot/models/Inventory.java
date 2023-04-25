package bot.models;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "inventories")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "inventory")
    private User user;

    @Column(name = "currency", nullable = false)
    private int currency = 0;

    @ElementCollection
    @CollectionTable(name = "items", joinColumns = @JoinColumn(name = "inventory_id"))
    @MapKeyColumn(name = "name")
    @Column(name = "amount", nullable = false)
    private Map<String, Integer> items = new HashMap<>() {{
        put("salad", 0);
        put("taco", 0);
        put("steak", 0);
        put("water", 0);
        put("lemonade", 0);
        put("cacao", 0);
    }};

    public Inventory() { }

    public Inventory(int currency, Map<String, Integer> items) {
        this.currency = currency;
        this.items = items;
    }

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
}