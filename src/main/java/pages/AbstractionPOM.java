package pages;


import configuration.BaseDriver;
import configuration.BaseTest;
import org.openqa.selenium.support.PageFactory;

public class AbstractionPOM extends BaseTest {

	// Default constructor
	public AbstractionPOM(BaseDriver baseDriver) {
		this.baseDriver = baseDriver;
		// This initElements method will create all WebElements
		PageFactory.initElements(baseDriver.driver, this);
	}

	public void updateBDriver(BaseDriver baseDriver) {
		this.baseDriver = baseDriver;
		// This initElements method will create all WebElements
				PageFactory.initElements(baseDriver.driver, this);
	}
	
}
