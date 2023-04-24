package bot.models;

import jakarta.persistence.*;

@Entity
@Table(name = "alpacas")
public class Alpaca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "outfit", nullable = false)
    private String outfit = "default";

    @Column(name = "nickname", nullable = false)
    private String nickname = "alpaca";

    @Column(name = "hunger", nullable = false)
    private int hunger = 100;

    @Column(name = "thirst", nullable = false)
    private int thirst = 100;

    @Column(name = "energy", nullable = false)
    private int energy = 100;

    @Column(name = "joy", nullable = false)
    private int joy = 100;

    public Alpaca() { }

    public Alpaca(String outfit, String nickname, int hunger, int thirst, int energy, int joy) {
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