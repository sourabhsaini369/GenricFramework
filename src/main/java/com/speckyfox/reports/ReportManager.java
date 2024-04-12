package com.speckyfox.reports;

import com.speckyfox.enums.EnumConstants.ReportType;

public class ReportManager {

	public static IReport getReportType() {

		ReportType getReportTypeEnumBasedOnPropertyFile = ReportFactory.getInstance().getReportEnum();
		return ReportFactory.getInstance().createObject(getReportTypeEnumBasedOnPropertyFile);

	}

}
