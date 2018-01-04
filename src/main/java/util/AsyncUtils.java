package main.java.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * @author GLK
 */
public final class AsyncUtils {

	private static final Logger log = Logger.getLogger(AsyncUtils.class.getName());

	private AsyncUtils() {
	}

	public static <T> T getResultFromAsyncTask(Future<T> task) {
		try {
			return task.get();
		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

	public static void waitForAsyncTaskCompletion(Future<?> task) {
		try {
			task.get();
		} catch (Exception e) {
			handleException(e);
		}
	}

	private static void handleException(Exception e) {
		if (e instanceof InterruptedException) {
			log.severe(e.getMessage());
		}
		if (e instanceof ExecutionException) {
			log.severe(e.getMessage());
		}
	}
}
