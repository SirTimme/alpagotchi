package dev.sirtimme.alpagotchi.shop.drinks;

import dev.sirtimme.alpagotchi.shop.IConsumable;

public class Water implements IConsumable {
    @Override
    public String getName() {
        return "water";
    }

    @Override
    public int getPrice() {
        return 10;
    }

    @Override
    public int getSaturation() {
        return 10;
    }

    @Override
    public String getType() {
        return "drink";
    }
}