package Bot.Utils;

public enum Error {
	NOT_INITIALIZED, MISSING_ARGS, NaN;

	public String getMessage(String prefix, String cmdName) {
		return switch (this) {
			case NOT_INITIALIZED -> Emote.REDCROSS + " You don't own an alpaca, use **" + prefix + "init** first";
			case MISSING_ARGS -> Emote.REDCROSS + " Too few arguments, please use **" + prefix + "help " + cmdName + "** for further information";
			case NaN -> Emote.REDCROSS + " Please specify a number";
		};
	}
}
