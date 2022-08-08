package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;

public class Entry {
    @BsonProperty(value = "_id")
    private final long memberID;
    private final Alpaca alpaca;
    private final Cooldown cooldown;
    private final Inventory inventory;

    public Entry(final long memberID) {
        this.memberID = memberID;
        this.alpaca = new Alpaca("default", "alpaca", 100, 100, 100, 100);
        this.cooldown = new Cooldown(0L, 0L);
        this.inventory = new Inventory(0, new HashMap<>() {{
            put("salad", 0);
            put("taco", 0);
            put("steak", 0);
            put("water", 0);
            put("lemonade", 0);
            put("cacao", 0);
        }});
    }

    @BsonCreator
    public Entry(@BsonProperty(value = "_id") final long memberID,
            @BsonProperty(value = "alpaca") final Alpaca alpaca,
            @BsonProperty(value = "cooldown") final Cooldown cooldown,
            @BsonProperty(value = "inventory") final Inventory inventory
    ) {
        this.memberID = memberID;
        this.alpaca = alpaca;
        this.cooldown = cooldown;
        this.inventory = inventory;
    }

    public long getMemberID() {
        return this.memberID;
    }

    public Alpaca getAlpaca() {
        return this.alpaca;
    }

    public Cooldown getCooldown() {
        return this.cooldown;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    @BsonIgnore
    public String getNickname() {
        return this.alpaca.getNickname();
    }

    @BsonIgnore
    public void setNickname(final String nickname) {
        this.alpaca.setNickname(nickname);
    }

    @BsonIgnore
    public String getOutfit() {
        return this.alpaca.getOutfit();
    }

    @BsonIgnore
    public void setOutfit(final String outfit) {
        this.alpaca.setOutfit(outfit);
    }

    @BsonIgnore
    public int getHunger() {
        return this.alpaca.getHunger();
    }

    @BsonIgnore
    public int getThirst() {
        return this.alpaca.getThirst();
    }

    @BsonIgnore
    public int getStat(final String stat) {
        return stat.equals("hunger") ? this.alpaca.getHunger() : this.alpaca.getThirst();
    }

    @BsonIgnore
    public void setStat(final String stat, final int newValue) {
        if (stat.equals("hunger")) {
            this.alpaca.setHunger(newValue);
        } else {
            this.alpaca.setThirst(newValue);
        }
    }

    @BsonIgnore
    public int getEnergy() {
        return this.alpaca.getEnergy();
    }

    @BsonIgnore
    public void setEnergy(final int energy) {
        this.alpaca.setEnergy(energy);
    }

    @BsonIgnore
    public int getJoy() {
        return this.alpaca.getJoy();
    }

    @BsonIgnore
    public void setJoy(final int joy) {
        this.alpaca.setJoy(joy);
    }

    @BsonIgnore
    public long getWork() {
        return this.cooldown.getWorkMinutes();
    }

    @BsonIgnore
    public void setWork(final long work) {
        this.cooldown.setWork(work);
    }

    @BsonIgnore
    public long getSleep() {
        return this.cooldown.getSleepMinutes();
    }

    @BsonIgnore
    public void setSleep(final long sleep) {
        this.cooldown.setSleep(sleep);
    }

    @BsonIgnore
    public int getCurrency() {
        return this.inventory.getCurrency();
    }

    @BsonIgnore
    public void setCurrency(final int currency) {
        this.inventory.setCurrency(currency);
    }

    @BsonIgnore
    public int getItem(final String item) {
        return this.inventory.getItemByName(item);
    }

    @BsonIgnore
    public void setItem(final String name, final int amount) {
        this.inventory.setItem(name, amount);
    }
}