package Bot.Utils;

public enum Stat {
	HUNGER,	THIRST;

	public String getName() {
		return this.name().toLowerCase();
	}
}
