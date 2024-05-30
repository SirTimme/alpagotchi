package dev.sirtimme.alpagotchi.shop.foods;

import dev.sirtimme.alpagotchi.shop.IConsumable;

public class Steak implements IConsumable {
    @Override
    public String getName() {
        return "steak";
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
        return "food";
    }
}