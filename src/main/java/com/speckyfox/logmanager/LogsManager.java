package com.speckyfox.logmanager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LogsManager {

	private static final Object lock = new Object();

	private LogsManager() {

	}

	public static Logger getLogger() {
		Logger log = LogManager.getLogger(LogsManager.class);
		synchronized (lock) {
			if (log != null) {
				return log;
			} else {
				return LogManager.getLogger(LogsManager.class);
			}
		}
	}

}
