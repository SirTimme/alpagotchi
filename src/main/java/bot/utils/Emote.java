package bot.utils;

public enum Emote {
	REDCROSS("<:RedCross:782229279312314368>"),
	GREENTICK("<:GreenTick:782229268914372609>");

	private final String emoteID;

	Emote(String emoteID) {
		this.emoteID = emoteID;
	}

	@Override
	public String toString() {
		return emoteID;
	}
}
