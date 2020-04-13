package util;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports createExtentReportInstance() {
        String path = System.getProperty("user.dir") + "/reports/report.html";
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(path);
        htmlReporter.config().setAutoCreateRelativePathMedia(false);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setDocumentTitle("Extent Report Demo");
        htmlReporter.config().setReportName("Merchant Dashboard Test Suite Report");
        htmlReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setCSS(".step-details > img { border: 2px solid #ccc; display: block; margin-top: 5px;height: 30px;width: 50px;}");
        extent = new ExtentReports();
        try {
            extent.setSystemInfo("Organization Name", "Extent Report Demo");
            extent.setSystemInfo("QA Name", "Umesh Shukla");
            extent.setSystemInfo("Os Name", System.getProperty("os.name"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
            extent.setSystemInfo("Host Name", InetAddress.getLocalHost().getHostName());
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Test Execution Environment", getEnvironment());
            extent.setReportUsesManualConfiguration(true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        extent.attachReporter(htmlReporter);
        return extent;
    }

    private static String getEnvironment() {
        String env = System.getProperty("env");
        if(env != null) {
			if(env.contains("stable") || env.contains("dashboard")){
				return "Production";
			}
		}else{
			return "Development";
        }
        return null;
    }
}