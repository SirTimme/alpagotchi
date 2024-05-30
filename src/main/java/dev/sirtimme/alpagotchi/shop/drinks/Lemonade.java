package dev.sirtimme.alpagotchi.shop.drinks;

import dev.sirtimme.alpagotchi.shop.IConsumable;

public class Lemonade implements IConsumable {
    @Override
    public String getName() {
        return "lemonade";
    }

    @Override
    public int getPrice() {
        return 25;
    }

    @Override
    public int getSaturation() {
        return 25;
    }

    @Override
    public String getType() {
        return "drink";
    }
}