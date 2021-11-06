package bot.models;

public class Alpaca {
    private String outfit, nickname;
    private int hunger, thirst, energy, joy;

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

    public String getNickname() {
        return this.nickname;
    }

    public int getHunger() {
        return this.hunger;
    }

    public int getThirst() {
        return this.thirst;
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getJoy() {
        return this.joy;
    }

    public void setOutfit(String outfit) {
        this.outfit = outfit;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setJoy(int joy) {
        this.joy = joy;
    }
}
