package Bot.Utils;

import org.jetbrains.annotations.NotNull;

public class ThreadFactory implements java.util.concurrent.ThreadFactory {
	@Override
	public Thread newThread(@NotNull Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		return thread;
	}
}
