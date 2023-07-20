package bot.shop;

import bot.shop.drinks.Cacao;
import bot.shop.drinks.Lemonade;
import bot.shop.drinks.Water;
import bot.shop.foods.Salad;
import bot.shop.foods.Steak;
import bot.shop.foods.Taco;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ItemManager {
    private final Map<String, IConsumable> items;

    public ItemManager() {
        this.items = new TreeMap<>();
        this.items.put("salad", new Salad());
        this.items.put("taco", new Taco());
        this.items.put("steak", new Steak());
        this.items.put("water", new Water());
        this.items.put("lemonade", new Lemonade());
        this.items.put("cacao", new Cacao());
    }

    public IConsumable getItem(final String itemName) {
        return this.items.get(itemName);
    }

    public List<IConsumable> getItems() {
        return this.items.values().stream().toList();
    }
}