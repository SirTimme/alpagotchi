package Bot.Utils;

public class Language {
	public static <T> String handle(T amount, String word) {
		return amount.equals(1) ? amount + word : amount + word + "s";
	}
}
