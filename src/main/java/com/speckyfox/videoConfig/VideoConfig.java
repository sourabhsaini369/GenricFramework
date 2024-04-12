package com.speckyfox.videoConfig;

import java.io.File;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.constants.PathConstants;
import com.speckyfox.logmanager.LogsManager;

import atu.testrecorder.*;

public class VideoConfig {

	private static final Object lock = new Object();
	private static VideoConfig instance;
	private static Integer videoFolderDeleteCount = 0;

	private VideoConfig() {

	}

	ATUTestRecorder recorder;

	private void recordingStart(String method) {

		LogsManager.getLogger().info("-----Video Started for " + method + "-------");
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		Date date = new Date();
		try {
			recorder = new ATUTestRecorder(System.getProperty("user.dir") + PathConstants.scriptVideosFolderPath,
					method + "-" + dateFormat.format(date), false);
		} catch (Exception e) {
			LogsManager.getLogger().warn("Error in finding the location of the video.");

		}
		// To start video recording.
		try {
			recorder.start();
		} catch (Exception e) {
			LogsManager.getLogger().warn("Error in starting the video.");

		}
	}

	private void recordingEnd() {
		try {
			recorder.stop();
		} catch (Exception e) {
			LogsManager.getLogger().warn("Unable to stop the screen recording.");

		}
	}

	/* Delete the old video recording of the test cases */

	private boolean deleteVideosFromPath(String path) {

		if (videoFolderDeleteCount.equals(0)) {
			File directory = new File(path);
			File[] files = directory.listFiles();

			for (File file : files) {
				file.delete();
			}
			videoFolderDeleteCount++;
			return true;

		}
		return false;
	}

	public static VideoConfig getInstance() {

		synchronized (lock) {

			if (instance == null) {
				instance = new VideoConfig();
			}
			return instance;

		}

	}

	public void videoRecordingStart(String method, boolean folderDelete) {
		if (!ConfigConstants.videoRecordingFlag.equalsIgnoreCase("no")) {
			if (folderDelete) {
				VideoConfig.getInstance()
						.deleteVideosFromPath(System.getProperty("user.dir") + PathConstants.scriptVideosFolderPath);
			}
			VideoConfig.getInstance().recordingStart(method);
		}

	}

	public void videoRecordingEnd() {
		if (!ConfigConstants.videoRecordingFlag.equalsIgnoreCase("no")) {
			VideoConfig.getInstance().recordingEnd();
		}
	}

}
