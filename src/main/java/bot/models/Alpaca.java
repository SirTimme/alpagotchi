package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Alpaca {
    private String outfit;
    private String nickname;
    private int hunger;
    private int thirst;
    private int energy;
    private int joy;

    @BsonCreator
    public Alpaca(@BsonProperty(value = "outfit") final String outfit,
            @BsonProperty(value = "nickname") final String nickname,
            @BsonProperty(value = "hunger") final int hunger,
            @BsonProperty(value = "thirst") final int thirst,
            @BsonProperty(value = "energy") final int energy,
            @BsonProperty(value = "joy") final int joy
    ) {
        this.outfit = outfit;
        this.nickname = nickname;
        this.hunger = hunger;
        this.thirst = thirst;
        this.energy = energy;
        this.joy = joy;
    }

    public String getOutfit() {
        return this.outfit;
    }

    public void setOutfit(final String outfit) {
        this.outfit = outfit;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

    public int getHunger() {
        return this.hunger;
    }

    public void setHunger(final int hunger) {
        this.hunger = hunger;
    }

    public int getThirst() {
        return this.thirst;
    }

    public void setThirst(final int thirst) {
        this.thirst = thirst;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(final int energy) {
        this.energy = energy;
    }

    public int getJoy() {
        return this.joy;
    }

    public void setJoy(final int joy) {
        this.joy = joy;
    }
}
