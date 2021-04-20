package Bot.Utils;

public enum Error {
	NOT_INITIALIZED, MISSING_ARGS, NaN;

	public String getMessage(String prefix, String cmdName) {
		switch (this) {
			case NOT_INITIALIZED:
				return Emote.REDCROSS + " You don't own an alpaca, use **" + prefix + "init** first";
			case MISSING_ARGS:
				return Emote.REDCROSS + " Too few arguments, please use **" + prefix + "help " + cmdName + "** for further information";
			case NaN:
				return Emote.REDCROSS + " Please specify a number";
			default:
				return "";
		}
	}
}
