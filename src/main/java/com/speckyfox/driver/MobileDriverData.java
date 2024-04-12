package com.speckyfox.driver;

public class MobileDriverData {

	private String mobilePlatformMode;
	private String mobileRemoteModeType;

	public MobileDriverData(String mobilePlatformMode, String mobileRemoteModeType) {

		mobilePlatformMode = this.mobilePlatformMode;
		mobileRemoteModeType = this.mobileRemoteModeType;
	}

	public String getMobilePlatformMode() {
		return mobilePlatformMode;
	}

	public String getMobileRemoteModeType() {
		return mobileRemoteModeType;
	}

}
