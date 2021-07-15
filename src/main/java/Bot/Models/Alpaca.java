package Bot.Models;

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
        return outfit;
    }

    public String getNickname() {
        return nickname;
    }

    public int getHunger() {
        return hunger;
    }

    public int getThirst() {
        return thirst;
    }

    public int getEnergy() {
        return energy;
    }

    public int getJoy() {
        return joy;
    }

    public void setOutfit(String outfit) {
        this.outfit = outfit;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHunger(int hunger) {
        this.hunger = this.hunger + hunger;
    }

    public void setThirst(int thirst) {
        this.thirst = this.thirst + thirst;
    }

    public void setEnergy(int energy) {
        this.energy = this.energy + energy;
    }

    public void setJoy(int joy) {
        this.joy = this.joy + joy;
    }
}
