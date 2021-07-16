package Bot.Models;

public class Entry {
    private final Alpaca alpaca;
    private final Cooldown cooldown;
    private final Inventory inventory;

    public Entry() {
        this.alpaca = new Alpaca();
        this.cooldown = new Cooldown();
        this.inventory = new Inventory();
    }

    public Alpaca getAlpaca() {
        return alpaca;
    }

    public Cooldown getCooldown() {
        return cooldown;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
