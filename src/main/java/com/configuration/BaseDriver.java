package com.configuration;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import com.utilities.FileConnector;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;

public class BaseDriver {

	public WebDriver driver = null;
	private final long limit;
	private String mainWindowHandle = null;
	protected Wait<WebDriver> newWait;

	public BaseDriver(WebDriver driver, long newWaitImplicit) {
		this.driver = driver;
		this.limit = newWaitImplicit;
		this.newWait = setWait();
		mainWindowHandle = driver
				.getWindowHandle();
	}

	/**
	 * Set wait
	 */
	public Wait<WebDriver> setWait() {
		newWait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(limit))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class)
				.ignoring(TimeoutException.class)
				.ignoring(InvalidElementStateException.class);
		return newWait;
	}

	public void changeStyleAttrWithElementID(String elementID, String TagName, String newValue) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String executeScriptText = "document.getElementById('" + elementID + "').setAttribute('" + TagName + "', '"
				+ newValue + "')";
		js.executeScript(executeScriptText);
	}

	public void setFocusOnBrowser() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.focus();");
	}

	public void resetBrowserSize() {
		Dimension d = new Dimension(1024, 768);
		driver.manage().window().setSize(d);
	}

	public void waitUntilPageIsLoaded() {
		ExpectedCondition<Boolean> pageLoadCondition = WebaseDriver -> {
			if (null == driver) {
				return false;
			}
			String complete = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
			return null != complete && complete.equalsIgnoreCase("complete");
		};

		newWait.until(pageLoadCondition);

	}

	public void switchToFrame(WebElement we) {
		System.out.println("switchToFrame");
		driver.switchTo().frame(we);
	}

	public void switchToDefault() {
		System.out.println("switch To Default Frame");
		driver.switchTo().defaultContent();
	}

	public void inputTextToiFrame(WebElement we, String expText) {
		System.out.println("inputTextToiFrame");
		switchToFrame(we);
		switchToInnerFrame(we, expText);
		switchToDefault();
	}

	public void switchToInnerFrame(WebElement we, String expText) {
		System.out.println("switchToInnerFrame");
		we.click();
		Actions act = new Actions(driver);
		act.sendKeys(we, expText).build().perform();
	}

	public void verifyLabelPresentInList(WebElement we, String expLabel) {
		System.out.println("verifyLabelPresentInList");
		String[] actList = getAllListItems(we);
		Assert.assertTrue(Arrays.asList(actList).contains(expLabel), "Text" + expLabel + "is not present in list!");
	}

	public void verifyLabelNotPresentInList(WebElement we, String expLabel) {
		System.out.println("verifyLabelNotPresentInList");
		String[] actList = getAllListItems(we);
		Assert.assertFalse(Arrays.asList(actList).contains(expLabel), "Text" + expLabel + "is present in list!");
	}

	public void clickAndWait(WebElement we, boolean waitForPageLoad) {
		we.click();
		if (waitForPageLoad) {
			waitUntilPageIsLoaded();
		}
	}

	public void clickAndWait(WebElement we) {

		System.out.println("Click On " + we);
		clickAndWait(we, true);
	}

	public void doubleClickAndWait(WebElement we, boolean waitForPageLoad) {
		System.out.println("doubleClickAndWait, newWait?=%s");
		(new Actions(driver)).doubleClick(we).perform();
		if (waitForPageLoad) {
			waitUntilPageIsLoaded();
		}

	}

	public void doubleClickAndWait(WebElement we) {
		doubleClickAndWait(we, true);
	}

	public void inputText(WebElement we, String text) {
		we.clear();
		we.sendKeys(text);
	}

	public void pressTab(WebElement we) {
		System.out.println("pressTab");
		we.click();
		we.sendKeys(Keys.TAB);
	}

	public void inputTextAndDefocus(WebElement we, String text) {
		
		we.clear();
		we.sendKeys(text);
		((JavascriptExecutor) driver).executeScript("arguments[0].onblur();", we);
		// element.sendKeys(Keys.TAB);
		waitUntilPageIsLoaded();
	}

	public void executeBlurEvent(WebElement we) {
		((JavascriptExecutor) driver).executeScript("arguments[0].onblur();", we);
	}

	public void inputTextAndChangefocus(WebElement we, String text) {
		
		we.clear();
		we.sendKeys(text);
		we.sendKeys(Keys.TAB);
		waitUntilPageIsLoaded();
	}

	public void sendKey(WebElement we, Keys key) {
		
		we.sendKeys(key);
		waitUntilPageIsLoaded();
	}

	public void setText(WebElement we, String text) {
		
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].value='" + text + "';", we);
	}

	public void setInnerHTML(WebElement we, String text) {
		
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].innerHTML='" + text + "';", we);
	}

	public void setTextContent(WebElement we, String text) {
		
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].textContent='" + text + "';", we);
	}

	public void waitForDropDownToLoad(final WebElement elementId) {
		System.out.println("waitForDropDownToLoad");

		ExpectedCondition<Boolean> newWaitCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				Object ret = ((JavascriptExecutor) driver)
						.executeScript("return document.getElementById('" + elementId + "').length > 1");
				return ret.equals(true);
			}
		};
		newWait.until(newWaitCondition);

	}

	public void waitForElementToBeClickable(WebElement webElement, int implicitTime) {
		System.out.println("waitForElementToBeClickable");

		newWait.until(ExpectedConditions.elementToBeClickable(webElement));
	}

	public void waitForElementState(WebElement webElement, int implicitTime, boolean elementState) {
		System.out.println("waitForElementState");

		newWait.until(ExpectedConditions.elementSelectionStateToBe(webElement, elementState));
	}

	public void waitForTextChangeInElement(WebElement we) {
		System.out.println("waitForTextChangeInElement");
		String actText = getText(we);

		waitForTextNotInElement(we, actText);
	}

	public void waitForTextNotInElement(WebElement we, String text) {
		System.out.println("waitForTextNotInElement");

		newWait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(we, text)));



	}

	public void waitForElement(WebElement we) {
		newWait.until(ExpectedConditions.elementToBeClickable(we));

	}

	public void waitForValueNotInElement(WebElement we, String text) {
		System.out.println("waitForValueNotInElement");
		newWait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementValue(we, text)));
	}

	public void waitForTextInElement(WebElement we, String expText) {
		System.out.println("waitForTextInElement");
		newWait.until(ExpectedConditions.textToBePresentInElement(we, expText));
	}

	public void waitForTextToBePresentInElement(WebElement we, String expText) {
		System.out.println("waitForTextInElementWithAttribute");
		newWait.until(ExpectedConditions.textToBePresentInElement(we, expText));
	}

	public void waitForTextToBePresentInElementWithAttribute(WebElement we, String expText, String attributeName) {
		System.out.println("waitForTextToBePresentInElementWithAttribute");

		int attempts = 0;
		while (attempts < 500) {
			try {
				if (we.getAttribute(attributeName).contains(expText)) {
					break;
				}
			} catch (Exception e) {
				System.err.println(e);
			}
			attempts++;
		}
	}

	public void waitForValueInElement(WebElement we, String expText) {
		System.out.println("waitForValueInElement");
		newWait.until(ExpectedConditions.textToBePresentInElementValue(we, expText));
	}

	public void waitForJSCondition(final String jsCondition) {
		
		ExpectedCondition<Boolean> newWaitCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				Object ret = ((JavascriptExecutor) driver).executeScript("return " + jsCondition);
				return ret.equals(true);
			}
		};
		newWait.until(newWaitCondition);

	}

	public boolean isVisible(WebElement we) {
		System.out.println("isVisible");
		return we.isDisplayed();
	}

	public String getCurrentUrl() {
		System.out.println("getCurrentUrl");
		return driver.getCurrentUrl();
	}

	public boolean isSelected(WebElement we) {
		System.out.println("isSelected");
		return we.isSelected();
	}

	public void selectCheckbox(WebElement we) {
		System.out.println("selectCheckbox");
		if (!isSelected(we)) {
			System.out.println("Element not checked, perform click");
			clickAndWait(we, false);
		} else {
			System.out.println("Element already checked, skipping click");
		}
	}

	public void unselectCheckbox(WebElement we) {
		System.out.println("unselectCheckbox");
		if (isSelected(we)) {
			System.out.println("Element checked, perform click");
			clickAndWait(we, false);
		} else {
			System.out.println("Element already unchecked, skipping click");
		}
	}

	public String getText(WebElement we) {
		System.out.println(we.getText());
		String message = we.getText();
		return message;
	}

	public WebDriver wd() {
		return driver;
	}

	public void quit() {
		System.out.println("Driver Quit");
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	public String getTextByAttribute(WebElement we, String attribute) {
		String message = we.getAttribute(attribute);

		return message;
	}

	public void selectByLabel(WebElement we, String label) {

		Select select = new Select(we);
		select.selectByVisibleText(label);
	}

	public void selectByIndex(WebElement we, int index) {

		Select select = new Select(we);
		select.selectByIndex(index);
	}

	public String getSelectedLabel(WebElement we) {
		System.out.println("getSelectedLabel");
		Select select = new Select(we);
		String actLabel = select.getFirstSelectedOption().getText();

		return actLabel;
	}

	public void verifySelectedLabel(WebElement we, String expLabel) {

		String actLabel = getSelectedLabel(we);

		Assert.assertEquals(actLabel, expLabel, "Selected label mismatch!");
	}

	public void verifyText(WebElement we, String expText) {
		System.out.println("verifyText");
		String actText = getText(we);
		Assert.assertEquals(actText, expText, "Element text mismatch!");
	}

	public void verifyTextNotInElement(WebElement we, String expText) {
		System.out.println("verifyTextNotInElement");
		String actText = getText(we);
		Assert.assertNotEquals(actText, expText, "Element text mismatch!");
	}

	public String getValue(WebElement we) {
		System.out.println("getValue");
		String actValue = we.getAttribute("value");

		return actValue;
	}

	public void verifyValue(WebElement we, String expValue) {
		System.out.println("verifyValue");
		String actValue = getValue(we);
		Assert.assertEquals(actValue, expValue, "Element value mismatch!");
	}

	public void captureFailTestScreenshot(ExtentTest test,String msg) {
		try {
			String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
			test.addScreenCaptureFromBase64String(base64Screenshot, msg);
			test.log(Status.FAIL, msg + ".\n Please find below attached screenshot");
		} catch (Exception e) {
			test.log(Status.FAIL, "Error while taking screenshot " + e.getMessage());
		}
	}

	public void waitForAllElementsVisible(List<WebElement> weLst) throws InterruptedException {
		newWait.until(ExpectedConditions.visibilityOfAllElements(weLst));
	}

	public void waitForElementVisible(WebElement we) {
		newWait.until(ExpectedConditions.visibilityOf(we));
	}
	public void waitForElementNotVisible(WebElement we) {
		newWait.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(we)));
	}
	public void verifyIsNotVisible(WebElement we) {
		System.out.println("verifyIsNotVisible");
		if (!isVisible(we)) {
			Assert.assertFalse(isVisible(we), "Element '" + we + "' is found visible!");
		} else {
			Assert.assertTrue(isVisible(we), "Element count is non-zero!");
		}
	}

	public void verifyIsSelected(WebElement we) {
		System.out.println("verifyIsSelected");
		Assert.assertTrue(isSelected(we), "Element '" + we + "' is not selected!");
	}

	public void verifyIsNotSelected(WebElement we) {
		System.out.println("verifyIsNotSelected");
		Assert.assertFalse(isSelected(we), "Element '" + we + "' is found selected!");
	}

	public void verifyTextBoxNotEmpty(WebElement we) {
		System.out.println("verifyTextBoxNotEmpty");
		Assert.assertNotSame(we.getAttribute("value"), "", "Element '" + we + "' Text Box is empty ");
	}

	public void verifyElementTextNotEmpty(WebElement we) {
		System.out.println("verifyElementTextNotEmpty");
		Assert.assertNotSame(we.getText(), "", "Element '" + we + "' Text is empty ");
	}

	public void scrollBottom() {
		System.out.println("scrollBottom");
		((JavascriptExecutor) driver).executeScript(
				"window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight))");
	}

	public void alertConfirm(boolean switchBackToMainWindow) {

		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();

			sleep(2000, "Required time to settle down after alert in IE");
		} catch (Exception e) {
			System.err.println(e);
		}

		if (switchBackToMainWindow) {
			driver.switchTo().defaultContent();
		}
	}

	public void promptConfirm(String sendKeys) {
		try {
			Alert promptAlert  = driver.switchTo().alert();
			//Send some text to the alert
			promptAlert .sendKeys(sendKeys);
			sleep(2000, "Required time to settle down after alert in IE");
		}

		catch (Exception e) {
			System.err.println(e);
		}
	}
	public void alertConfirm() {
		alertConfirm(true);
	}

	public void alertDismiss() {
		System.out.println("alertDismiss");

		try {
			Alert alert = driver.switchTo().alert();
			alert.dismiss();

			// Required time to settle down after alert in IE
			sleep(2000, "Required time to settle down after alert in IE");
		} catch (Exception e) {
			System.err.println(e);
		}

		driver.switchTo().defaultContent();
	}

	public void switchToNewWindow() {
		System.out.println("switchToNewWindow");

		int trials = 3;

		Set<String> windowIds;
		do {
			windowIds = driver.getWindowHandles();
			if (trials != 3) {
				sleep(1000, "Requires time to select newly opened window");
			}
		} while (windowIds.size() <= 1 && --trials > 0);

		// Switch to new window opened
		for (String winHandle : windowIds) {
			if (!winHandle.equals(mainWindowHandle)) {

				driver.switchTo().window(winHandle);
			}
		}
	}

	public void switchToMainWindow() {
		Assert.assertNotNull(mainWindowHandle, "Main Window Handle not initialized!");

		driver.switchTo().window(mainWindowHandle);
	}

	public void closeWindow() {
		System.out.println("closeWindow");

		driver.close();
	}

	public void verifyWindowTitle(String title) {
		String actTitle = getTitle();

		Assert.assertEquals(actTitle, title, "Window title mismatch!");
	}

	public void mouseHoverClickOnElement(WebElement ele) {
		Actions action = new Actions(driver);
		System.out.println("mouseHover And Click On "+ele.getText());
		action.moveToElement(ele).click().build().perform();
	}

	public void clickOnElementOnSpecificDimesion(WebElement clickOnTopSelectMenu) {
		Actions action = new Actions(driver);
		int xyard=clickOnTopSelectMenu.getSize().width-10;
		int yard=clickOnTopSelectMenu.getSize().height-5;
		action.moveToElement(clickOnTopSelectMenu,xyard,yard).perform();
		action.moveToElement(clickOnTopSelectMenu,xyard,yard).clickAndHold().release().build().perform();

	}

	public void clickOnCheckBoxOnSpecificDimesion(WebElement clickOnTopSelectMenu) {
		Actions action = new Actions(driver);
		int xyard=clickOnTopSelectMenu.getSize().width/2;
		int yard=clickOnTopSelectMenu.getSize().height/2;
		action.moveToElement(clickOnTopSelectMenu,xyard,yard).perform();
		action.moveToElement(clickOnTopSelectMenu,xyard,yard).clickAndHold().release().build().perform();
	}

	public void dragAndDrop(WebElement sourceWE, WebElement targetWE) {
		System.out.println("dragAndDrop");
		(new Actions(driver)).dragAndDrop(sourceWE, targetWE).perform();
	}

	public void refreshPage() {
		System.out.println("refreshPage");
		driver.navigate().refresh();
	}

	public void bakcPage() {
		driver.navigate().back();
	}

	public void sleep(long milliseconds, String reasonForSleep) {
		Assert.assertNotEquals("", reasonForSleep.trim(), "Reason for sleep not specified!");

		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void gotoUrl(String urlAddress) {
		System.out.println("gotoUrl: " + urlAddress);
		driver.get(urlAddress);
		waitUntilPageIsLoaded();
	}

	public void maximizeWindow() {
		System.out.println("Maximize window");
		driver.manage().window().maximize();
	}

	public void verifyElementContainsText(WebElement we, String expLabel) {

		String actText = getText(we);
		Assert.assertTrue(actText.contains(expLabel), "Expected String is not present");
	}

	public void verifyElementTextIsSubstring(WebElement we, String expLabel) {

		String actText = getText(we);

		Assert.assertTrue(expLabel.contains(actText), "Expected String is not present");
	}

	public String alertGetMessage() {
		System.out.println("getAlertMessage");
		String msg = "";
		try {
			Alert alert = driver.switchTo().alert();
			msg = alert.getText();

		} catch (Exception e) {
			System.err.println(e);
		}

		return msg;

	}

	public void verifyElementTextMatchesRegex(WebElement we, String regex) {

		String actText = getText(we);
		Assert.assertTrue(actText.matches(regex), "Actual String does not matches Regex");
	}

	public String[] getAllListItems(WebElement we) {
		System.out.println("getAllListItems");

		Select select = new Select(we);
		List<WebElement> options = select.getOptions();
		int j = 0;
		String[] val = new String[options.size()];
		for (WebElement weOption : options) {
			val[j++] = weOption.getText();
		}


		return val;
	}

	public void verifyAllListItems(WebElement we, String[] items) {
		String expItemsStr = FileConnector.getStringFromArray(items);
		String[] actItems = getAllListItems(we);

		Assert.assertEquals(actItems.length, items.length, "List items length mismatch!");
		Assert.assertEquals(FileConnector.getStringFromArray(actItems), expItemsStr, "List items mismatch!");
	}

	public boolean isPresent(WebElement we) {
		System.out.println("isPresent");
		boolean present = we.isDisplayed();

		return present;
	}

	public String getTitle() {
		System.out.println("getTitle");
		String actTitle = driver.getTitle();

		return actTitle;
	}

	public void waitForAlert() {
		System.out.println("waitForAlert");

		newWait.until(ExpectedConditions.alertIsPresent());
	}

	public void verifyColor(WebElement we, String expcolor) {

		String color = we.getCssValue("color");

		String[] numbers = color.replace("rgba(", "").replace(")", "").split(",");
		int r = Integer.parseInt(numbers[0].trim());
		int g = Integer.parseInt(numbers[1].trim());
		int b = Integer.parseInt(numbers[2].trim());
		String hex = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
		Assert.assertEquals(hex.toUpperCase(), expcolor.toUpperCase(),
				"Element text color mismatch! Expected:" + expcolor.toUpperCase() + "actual:" + hex.toUpperCase());

	}

	public void verifyBackgroundColor(WebElement we, String expcolor) {
		String color = we.getCssValue("background-color");
		String[] numbers = color.replace("rgba(", "").replace(")", "").split(",");
		int r = Integer.parseInt(numbers[0].trim());
		int g = Integer.parseInt(numbers[1].trim());
		int b = Integer.parseInt(numbers[2].trim());
		String hex = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
		Assert.assertEquals(hex.toUpperCase(), expcolor.toUpperCase(), "Element background color mismatch! Expected:"
				+ expcolor.toUpperCase() + "actual:" + hex.toUpperCase());

	}

	public void verifyElementValueIsEmpty(WebElement we) {
		System.out.println("verifyElementValueIsEmpty");
		Assert.assertEquals(getValue(we), "", "Element '" + we + "' Value is not empty");
	}

	public void openDuplicateWindow() {
		System.out.println("openDuplicateWindow");
		((JavascriptExecutor) driver).executeScript("(window.open(document.URL))");
	}

	public void verifyTextBoxIsNotEditable(WebElement we) {
		System.out.println("verifyTextBoxIsNotEditable");
		String value = we.getAttribute("readonly");
		Assert.assertTrue(value.contentEquals("true"), "Element '" + we + "' Is Editable");
	}

	public void verifyTextIsUnderLined(WebElement we) {
		System.out.println("verifyTextBoxIsNotEditable");
		String value = we.getAttribute("style");
		Assert.assertTrue(value.contains("text-decoration: underline"),
				"Text of Element '" + we + "' Is not Underlined");

	}

	public void uploadFile(WebElement we, String path) {
		System.out.println("uploadFile");
		we.sendKeys(path);
	}

	public void waitForElementTextNotEmpty(WebElement we) {
		System.out.println("waitForElementTextNotEmpty");

		int trials = 10;
		int len;

		do {
			String text = getText(we);
			len = text.length();
			if (trials != 10) {
				System.out.println("Waiting");
				sleep(2000, "Requires time for text to be displayed");
			}
		} while (len <= 1 && --trials > 0);

	}

	public void waitForElementValueNotEmpty(WebElement we) {
		System.out.println("waitForElementValueNotEmpty");

		int trials = 10;
		int len;

		do {
			String text = getValue(we);
			len = text.length();
			if (trials != 10) {
				System.out.println("Waiting");
				sleep(2000, "Requires time for text to be displayed");
			}
		} while (len <= 1 && --trials > 0);

	}

	public void clearText(WebElement we) {
		System.out.println("clearText");
		we.clear();
	}

	public void verifyElementContainsValue(WebElement we, String expLabel) {

		String actValue = getValue(we);
		Assert.assertTrue(actValue.contains(expLabel), "Expected String is not present");

	}

	public void verifyElementValueNotEmpty(WebElement we) {
		System.out.println("verifyElementValueNotEmpty");

		Assert.assertNotEquals(getValue(we), "", "Element '" + we + "' Value is empty");
	}

	public void selectRadioButton(List<WebElement> weLst, int optionNo) {
		System.out.println("selectRadioButton");
		if (optionNo > 0 && optionNo <= weLst.size()) {
			weLst.get(optionNo - 1).click();
		} else {
			throw new NotFoundException("option " + optionNo + " not found");
		}
	}

	public void verifySelectionBoxIsMultiple(String elementId) {
		System.out.println("verifyIsMultipleSelectionBox");

		Object isMultiple = ((JavascriptExecutor) driver)
				.executeScript("return document.getElementById('" + elementId + "').multiple");
		Assert.assertEquals(isMultiple, true, "Element is not Multiple Selection Box");
	}

	public int getselectedRadioButton(List<WebElement> weLst) {
		System.out.println("getselectedRadioButton");

		int optionNo = 0;
		for (int i = 0; i <= weLst.size(); i++) {
			if (weLst.get(i).isSelected()) {
				optionNo = (i + 1);
				break;
			}
		}

		return optionNo;
	}

	public void verifyJSCondition(final String jsCondition, boolean status) {
		Object ret = ((JavascriptExecutor) driver).executeScript("return " + jsCondition);
		Assert.assertEquals(status, ret, "Status for jsCondition :" + jsCondition + " is not '" + status + "'");

	}

	public void executeJSCondition(final String jsCondition) {
		((JavascriptExecutor) driver).executeScript(jsCondition);
	}

	public void verifyScrollBarPresent(WebElement elementId) {
		System.out.println("verifyScrollBarPresent");
		String condition = "document.getElementById('" + elementId + "').scrollHeight > document.getElementById('"
				+ elementId + "').clientHeight";
		Object ret = ((JavascriptExecutor) driver).executeScript("return " + condition);
		Assert.assertEquals(ret, true, "Scroll Bar with 'element Id' :" + elementId + " is not present!");

	}


	public void scrollUntilElementIsView(WebElement ele) {
		this.waitForElementVisible(ele);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
	}

	public void waitForChangeInLengthOfValue(final WebElement we, final int valueLen) {
		System.out.println("waitForChangeInLengthOfValue");

		ExpectedCondition<Boolean> newWaitCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				boolean flag = false;
				if (we.getAttribute("value").length() != valueLen) {
					flag = true;
				}
				return flag;
			}
		};

		newWait.until(newWaitCondition);
	}

	public void appendText(WebElement we, String text) {
		we.sendKeys(text);
	}

	public void verifyScrollBarNotPresent(String elementId) {
		System.out.println("verifyScrollBarPresent");
		String condition = "document.getElementById('" + elementId + "').scrollHeight > document.getElementById('"
				+ elementId + "').clientHeight";
		Object ret = ((JavascriptExecutor) driver).executeScript("return " + condition);
		Assert.assertEquals(ret, false, "Scroll Bar with 'element Id' :" + elementId + " is present!");
	}

	public void waitForWindowToClose() {
		final int windowCount = driver.getWindowHandles().size();

		System.out.println("waitForWindowToClose");
		if (windowCount > 1) {
			ExpectedCondition<Boolean> windowClosedCondition = new ExpectedCondition<>() {
				public Boolean apply(WebDriver driver) {
					assert driver != null;
					return driver.getWindowHandles().size() < windowCount;
				}
			};

			newWait.until(windowClosedCondition);
		}
		switchToMainWindow();


	}

	public boolean isAlertPresent() {
		boolean presentFlag = false;

		try {
			System.out.println("isAlertPresent");

			if (newWait.until(ExpectedConditions.alertIsPresent()) != null) {
				presentFlag = true;
			}

		} catch (NoAlertPresentException ex) {
			// Alert not present
			System.err.println(ex);
		}
		return presentFlag;
	}

	public void selectByValue(WebElement we, String value) {
		this.sleep(2000,"For showing case Create page");

		Select select = new Select(we);
		select.selectByValue(value);
	}

	public void selectByVisibleText(WebElement we, String value) {

		Select select = new Select(we);
		select.selectByVisibleText(value);
	}

	public void verifyIsEnabled(WebElement we) {
		System.out.println("verifyIsEnabled");

		Assert.assertTrue(isEnabled(we), "Element '" + we + "' is not enabled!");
	}

	public void verifyIsDisabled(WebElement we) {
		System.out.println("verifyIsDisabled");
		Assert.assertFalse(isEnabled(we), "Element '" + we + "' is not enabled!");
	}

	public boolean isEnabled(WebElement we) {
		System.out.println("isEnabled");
		return we.isEnabled();
	}

	public boolean isReadOnly(WebElement we) {
		System.out.println("isReadOnly");
		return Boolean.parseBoolean(we.getAttribute("readonly"));
	}

	public boolean findElementByXpath(String object) {
		System.out.println("findElementByXpath");
		try {
			driver.findElement(By.xpath(object));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getTextColor(WebElement we) {
		System.out.println("getTextColor");
		String color = we.getCssValue("color");

		if (!color.contains("#")) {
			String[] numbers = color.replace("rgba(", "").replace(")", "").split(",");
			int r = Integer.parseInt(numbers[0].trim());
			int g = Integer.parseInt(numbers[1].trim());
			int b = Integer.parseInt(numbers[2].trim());
			color = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
		}
		return color;
	}

	public void mouseHoverOnElement(WebElement we) {
		Actions action = new Actions(driver);
		action.moveToElement(we).moveToElement(we).build().perform();
		this.sleep(2000,"Check Is Hover Show Detail");
	}


	public void moveOnElement(WebElement we) {
		System.out.println("moveOnElement "+we.getText());

		Actions actions = new Actions(driver);
		actions.moveToElement(we);
		actions.perform();

	}

	public WebElement FindElementByCss(String object) {
		System.out.println("findElementByCSS");
		try {
            return driver.findElement(By.cssSelector(object));
		} catch (Exception e) {
			return null;
		}
	}

	public List<WebElement> FindElementsByCss(String object) {
		List<WebElement> element;
		try {
			element = driver.findElements(By.cssSelector(object));
			return element;
		} catch (Exception e) {
			return null;
		}

	}

	public List<String> getListItemFromString(String stringArray) {
		List<String> secondDropDownItemList;
		try {
			secondDropDownItemList = new ArrayList<>(Arrays.asList(stringArray.split(",")));
			return secondDropDownItemList;
		} catch (Exception e) {
			return null;
		}
	}

	public void selectItemFromListItem(List<WebElement> we,String selectItem) {
		try {
			boolean isitemGet=false;
			for(WebElement DropDownItem:we){
				if(DropDownItem.getText().trim().contains(selectItem)) {
					Actions action = new Actions(driver);
					System.out.println("mouseHoverClickOnElement "+selectItem);
					isitemGet=true;
					action.moveToElement(DropDownItem).click().build().perform();
					break;
				}
			}
			if(!isitemGet) {
				System.err.println("Selected item is Not Found");
			}
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
			System.err.println(e.getMessage());
		}
	}
	
	public void scrollHorizontalUntillElementIsView(WebElement ele) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(1000,0)", ele);
		System.out.println("Scroll Horzontal");
		this.sleep(2000,"Element find");
	}

	public void scrollHorizontalUntillListElementsIsView(List<WebElement> eleList) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(1000,0)", eleList);
		System.out.println("Scroll Horzontal");
		this.sleep(2000,"Element find");

	}

	public boolean isClickable(WebElement el) {
		try{
			newWait.until(ExpectedConditions.elementToBeClickable(el));
			return true;
		}
		catch (Exception e){
			return false;
		}
	}
}