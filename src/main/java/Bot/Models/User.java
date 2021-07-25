package Bot.Models;

public class User {
    private final long _id;
    private final Alpaca alpaca;
    private final Cooldown cooldown;
    private final Inventory inventory;

    public User(long memberID) {
        _id = memberID;
        alpaca = new Alpaca();
        cooldown = new Cooldown();
        inventory = new Inventory();
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
