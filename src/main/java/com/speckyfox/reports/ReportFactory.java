package com.speckyfox.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.speckyfox.constants.ConfigConstants;
import com.speckyfox.enums.EnumConstants.ReportType;

public class ReportFactory {

	private final Map<ReportType, Supplier<?>> ReportSelector;
	private IReport reportInstance;
	private static ReportFactory instance;
	private static final Object lock = new Object();

	private ReportFactory() {

		ReportSelector = new HashMap<>();
		ReportSelector.put(ReportType.EXTENT, ExtentIReport::getInstance);
		ReportSelector.put(ReportType.EXCEL, ExcelIReport::getInstance);

	}

	public static ReportFactory getInstance() {

		synchronized (lock) {

			if (instance == null) {
				instance = new ReportFactory();
			}
			return instance;

		}

	}

	public IReport createObject(ReportType reportType) {

		synchronized (this)

		{

			if (this.reportInstance == null) {
				return this.reportInstance = (IReport) ReportSelector.get(reportType).get();

			} else {
				return this.reportInstance;
			}

		}

	}

	public ReportType getReportEnum() {
		ReportType getReportFromPropertyFile = null;

		try {

			getReportFromPropertyFile = ReportType.valueOf(ConfigConstants.testingReportType.toUpperCase());

		} catch (IllegalArgumentException illegalException) {

			System.out.println("Please Provide the correct ReportType in Config.Property File");
		}
		return getReportFromPropertyFile;
	}

}
