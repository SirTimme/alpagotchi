package bot.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashMap;
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
    @CollectionTable(name = "items", joinColumns = @JoinColumn(name = "item_map_id"))
    @MapKeyColumn(name = "item")
    @Column(name = "amount")
    @Fetch(FetchMode.JOIN)
    private Map<String, Integer> items = new HashMap<>() {{
        put("salad", 0);
        put("taco", 0);
        put("steak", 0);
        put("water", 0);
        put("lemonade", 0);
        put("cacao", 0);
    }};

    public Inventory() { }

    public int getCurrency() {
        return this.currency;
    }

    public void setCurrency(final int currency) {
        this.currency = currency;
    }

    public Map<String, Integer> getItems() {
        return this.items;
    }

    public void setItems(final Map<String, Integer> items) {
        this.items = items;
    }
}