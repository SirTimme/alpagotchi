package Bot.Utils;

import Bot.Database.IDatabase;

import java.util.TimerTask;

public class DecreaseTask extends TimerTask {
	@Override
	public void run() {
		IDatabase.INSTANCE.decreaseValues();
	}
}
