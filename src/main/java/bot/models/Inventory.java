package bot.models;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "inventories")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "currency", nullable = false)
    private int currency = 0;

    @Column(name = "salad", nullable = false)
    private int salad = 0;

    @Column(name = "taco", nullable = false)
    private int taco = 0;

    @Column(name = "steak", nullable = false)
    private int steak = 0;

    @Column(name = "water", nullable = false)
    private int water = 0;

    @Column(name = "lemonade", nullable = false)
    private int lemonade = 0;

    @Column(name = "cacao", nullable = false)
    private int cacao = 0;

    public Inventory() { }

    public int getCurrency() {
        return this.currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getSalad() {
        return this.salad;
    }

    public void setSalad(int salad) {
        this.salad = salad;
    }

    public int getTaco() {
        return this.taco;
    }

    public void setTaco(int taco) {
        this.taco = taco;
    }

    public int getSteak() {
        return this.steak;
    }

    public void setSteak(int steak) {
        this.steak = steak;
    }

    public int getWater() {
        return this.water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getLemonade() {
        return this.lemonade;
    }

    public void setLemonade(int lemonade) {
        this.lemonade = lemonade;
    }

    public int getCacao() {
        return this.cacao;
    }

    public void setCacao(int cacao) {
        this.cacao = cacao;
    }
}