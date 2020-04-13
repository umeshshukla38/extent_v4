package demotest;
import config.Base;
import pages.DemoPage;
import org.testng.Assert;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DemoTest extends Base {
	DemoPage demo_page;
	private static org.slf4j.Logger log = LoggerFactory.getLogger(DemoTest.class);

	public DemoTest() {
		super();
	}

	@Test(priority = 1)
	public void navigateToPage(){
		demo_page = new DemoPage(driver);
		String url = prop.getProperty("baseurl");
		if(driver.getCurrentUrl().equals(url)){
			wait.until(ExpectedConditions.elementToBeClickable(demo_page.google_search));
			log.info("You are on "+driver.getTitle()+" page.");
		}
	}

	@Test(priority = 2)
	public void checkInputSearchPresent(){
		Assert.assertEquals(demo_page.input_box.isDisplayed(), true, "Input search is not present on page ...!");
	}

	@Test(priority = 3)
	public void checkSearchBtnPresent(){
		/** Intensely faile case.*/
		Assert.assertEquals(demo_page.google_search.isDisplayed(), false, "Search button is present on page but failed to show skipped test ...!");
	}

	@Test(priority = 4, dependsOnMethods = {"checkSearchBtnPresent"})
	public void checkPresentIamFeelingLucky(){
		/** Because checkSearchBtnPresent test failed so this should be in skipped */
		Assert.assertEquals(demo_page.im_feeling_lucky.isDisplayed(), true, "Feeling Lucky button is not present on page ...!");
	}
}
