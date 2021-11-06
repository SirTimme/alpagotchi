package bot.models;

public class Entry {
    private final long memberID;
    private final Alpaca alpaca;
    private final Cooldown cooldown;
    private final Inventory inventory;

    public Entry(long memberID, Alpaca alpaca, Cooldown cooldown, Inventory inventory) {
        this.memberID = memberID;
        this.alpaca = alpaca;
        this.cooldown = cooldown;
        this.inventory = inventory;
    }

    public long getMemberID() {
        return this.memberID;
    }

    public String getNickname() {
        return this.alpaca.getNickname();
    }

    public void setNickname(String nickname) {
        this.alpaca.setNickname(nickname);
    }

    public String getOutfit() {
        return this.alpaca.getOutfit();
    }

    public void setOutfit(String outfit) {
        this.alpaca.setOutfit(outfit);
    }

    public int getHunger() {
        return this.alpaca.getHunger();
    }

    public void setHunger(int hunger) {
        this.alpaca.setHunger(hunger);
    }

    public int getThirst() {
        return this.alpaca.getThirst();
    }

    public void setThirst(int thirst) {
        this.alpaca.setThirst(thirst);
    }

    public int getEnergy() {
        return this.alpaca.getEnergy();
    }

    public void setEnergy(int energy) {
        this.alpaca.setEnergy(energy);
    }

    public int getJoy() {
        return this.alpaca.getJoy();
    }

    public void setJoy(int joy) {
        this.alpaca.setJoy(joy);
    }

    public long getWork() {
        return this.cooldown.getWork();
    }

    public void setWork(long work) {
        this.cooldown.setWork(work);
    }

    public long getSleep() {
        return this.cooldown.getSleep();
    }

    public void setSleep(long sleep) {
        this.cooldown.setSleep(sleep);
    }

    public int getCurrency() {
        return this.inventory.getCurrency();
    }

    public void setCurrency(int currency) {
        this.inventory.setCurrency(currency);
    }

    public int getItem(String item) {
        return this.inventory.getItem(item);
    }

    public void setItem(String name, int amount) {
        this.inventory.setItem(name, amount);
    }
}

