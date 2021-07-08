package Bot.Models;

public class Entry {
    private final Alpaca alpaca;
    private final Cooldowns cooldowns;
    private final Inventory inventory;

    public Entry(Alpaca alpaca, Cooldowns cooldowns, Inventory inventory) {
        this.alpaca = alpaca;
        this.cooldowns = cooldowns;
        this.inventory = inventory;
    }

    public Alpaca getAlpaca() {
        return alpaca;
    }

    public Cooldowns getCooldowns() {
        return cooldowns;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
