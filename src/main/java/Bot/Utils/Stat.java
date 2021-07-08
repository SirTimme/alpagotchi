package Bot.Utils;

public enum Stat {
	HUNGER,	THIRST,	ENERGY,	JOY, NICKNAME, OUTFIT, CURRENCY, WORK, SLEEP, SALAD, TACO, STEAK, WATER, LEMONADE, CACAO;

	public String getName() {
		return this.name().toLowerCase();
	}
}
