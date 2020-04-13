package util;
import java.io.File;
import config.Base;
import java.util.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.io.IOException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class ExtentManagerListener implements ITestListener {
    
    ExtentTest parent;
    private static ExtentReports extent = ExtentManager.createExtentReportInstance();
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>(); // for multiple classes handelling.
    
    @Override
    public void onTestStart(ITestResult result) {
        parent = extent.createTest(result.getTestClass().getName()+ " :: "+result.getMethod().getMethodName());
        ExtentTest child = parent.createNode(result.getMethod().getMethodName());
        parent.getModel().setStartTime(getTime(result.getStartMillis()));
        extentTest.set(parent);
        extentTest.set(child);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().pass("Test Passed");
        String logText = "<b>Test Method "+result.getMethod().getMethodName()+" Successful.</b>";
        Markup markup = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        extentTest.get().log(Status.PASS, markup);
        parent.getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String exception_msg = Arrays.toString(result.getThrowable().getStackTrace());
        StringBuilder failMsgFormat = new StringBuilder();
        failMsgFormat.append("<details><summary><b><font color=red>"+"There is some error, click to see logs : "+ "</font></b></summary>");
        failMsgFormat.append(exception_msg.replaceAll(",", "<br>")+"</details> \n");
        extentTest.get().fail(failMsgFormat.toString());
        
        Object currentClass = result.getInstance();
        WebDriver driver = ((Base) currentClass).driver;
        String path = takeScreenshot(driver, methodName);
        try{
            MediaEntityModelProvider mediaModel = MediaEntityBuilder.createScreenCaptureFromPath(path).build();
            extentTest.get().fail("<b><font color=red>"+"Screenshot of Failure"+"</font></b>", mediaModel);
        }catch(IOException e){
            extentTest.get().fail("Test failed, Not able to attach screenshot.");
        }

        String logText = "<b>Test Method "+methodName+" Failed.</b>";
        Markup markup = MarkupHelper.createLabel(logText, ExtentColor.RED);
        extentTest.get().log(Status.FAIL, markup);
        parent.getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().pass("Test Skipped");
        String logText = "<b>Test Method "+result.getMethod().getMethodName()+" Skipped.</b>";
        Markup markup = MarkupHelper.createLabel(logText, ExtentColor.GREY);
        extentTest.get().log(Status.SKIP, markup);
        parent.getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        
    }

    @Override
    public void onStart(ITestContext context) {
    
    }

    @Override
    public void onFinish(ITestContext context) {
        if(extent != null){
            extent.flush();
        }
    }

    public static String takeScreenshot(WebDriver driver, String methodName){
        Date date = new Date();
        String fileName = methodName+"_"+date.toString().replace(":", "_").replace(" ", "_")+".png";
        String directory = System.getProperty("user.dir")+"/screenshots/";
        String path = directory+fileName;
        String os = System.getProperty("os.name");
        try{
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            if(os.equals("Linux") || os.contains("Mac")) {
                FileUtils.copyFile(scrFile, new File(path));
            }else {
                FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"\\screenshots\\"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return path;
    }

    public Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();      
    }
}