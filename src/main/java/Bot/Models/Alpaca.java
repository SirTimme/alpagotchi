package Bot.Models;

public class Alpaca {
    private String outfit, nickname;
    private int hunger, thirst, energy, joy;

    public Alpaca() {
        outfit = "default";
        nickname = "alpaca";
        hunger = 100;
        thirst = 100;
        energy = 100;
        joy = 100;
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
        this.hunger += hunger;
    }

    public void setThirst(int thirst) {
        this.thirst += thirst;
    }

    public void setEnergy(int energy) {
        this.energy += energy;
    }

    public void setJoy(int joy) {
        this.joy += joy;
    }
}
