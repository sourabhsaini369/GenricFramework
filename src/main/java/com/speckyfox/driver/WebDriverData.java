package com.speckyfox.driver;

public class WebDriverData {

	private String webBrowser;
	private String webRunMode;
	private String webRemoteModeType;

	public WebDriverData(String webBrowser, String webRunMode, String webRemoteModeType) {

		this.webBrowser = webBrowser;
		this.webRunMode = webRunMode;
		this.webRemoteModeType = webRemoteModeType;
	}

	public String getWebBrowser() {
		return webBrowser;

	}

	public String getWebRunMode() {
		return webRunMode;
	}

	public String getWebRemoteModeType() {
		return webRemoteModeType;
	}

}
