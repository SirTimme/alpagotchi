package Bot.Utils;

public enum Stat {
	HUNGER,	THIRST,	ENERGY,	JOY;

	public String getName() {
		return this.name().toLowerCase();
	}
}
