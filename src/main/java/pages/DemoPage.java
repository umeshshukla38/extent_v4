package pages;
import config.Base;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DemoPage extends Base {

	WebDriver driver;

	public DemoPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy (xpath = "//input[@name='q']")
	public WebElement input_box;

	@FindBy (xpath = "//div[3]/center/input[1]")
	public WebElement google_search;

	@FindBy (xpath = "//div[3]/center/input[2]")
	public WebElement im_feeling_lucky;

}