package Bot.Utils;

public enum Emote {
	REDCROSS("<:RedCross:827140649136029706>"),
	GREENTICK("<:GreenTick:827140711341097000>");

	private final String emoteID;

	Emote(String emoteID) {
		this.emoteID = emoteID;
	}

	@Override
	public String toString() {
		return emoteID;
	}
}
