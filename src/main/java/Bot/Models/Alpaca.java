package Bot.Models;

public class Alpaca {
    private final String outfit, nickname;
    private final int hunger, thirst, energy, joy;

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
}
