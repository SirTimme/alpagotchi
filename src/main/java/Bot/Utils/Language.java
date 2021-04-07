package Bot.Utils;

public class Language {
	public static String handle(Number amount, String word) {
		return amount + (amount.longValue() == 1 ? " " + word : " " + word + "s");
	}
}
