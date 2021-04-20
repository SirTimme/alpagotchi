package Bot.Utils;

public enum Stat {
	HUNGER,	THIRST,	ENERGY,	JOY, NICKNAME, OUTFIT, CURRENCY, WORK, SLEEP, PRICE, SATURATION, NAME;

	public String getName() {
		return this.name().toLowerCase();
	}
}
