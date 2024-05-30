package dev.sirtimme.alpagotchi.shop.drinks;

import dev.sirtimme.alpagotchi.shop.IConsumable;

public class Cacao implements IConsumable {
    @Override
    public String getName() {
        return "cacao";
    }

    @Override
    public int getPrice() {
        return 40;
    }

    @Override
    public int getSaturation() {
        return 40;
    }

    @Override
    public String getType() {
        return "drink";
    }
}