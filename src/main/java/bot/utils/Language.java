package bot.utils;

public enum Language {
	SINGULAR, PLURAL;

	public static String handle(Number amount, String singular, String plural) {
		return amount.longValue() == 1 ? singular : plural;
	}
}
