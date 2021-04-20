package Bot.Utils;

public class Language {
	public static String handle(Number amount, String word) {
		if (word.equals("fluffy")) {
			return amount + (amount.longValue() == 1 ? " " + word : " fluffies");
		}
		return amount + (amount.longValue() == 1 ? " " + word : " " + word + "s");
	}
}
