package com.magenic.jmaqs.selenium.pagemodels;

import com.magenic.jmaqs.selenium.baseSeleniumTest.SeleniumConfig;
import com.magenic.jmaqs.selenium.baseSeleniumTest.SeleniumTestObject;
import org.openqa.selenium.By;
import org.testng.Assert;

/**
 * The type Login page model.
 */
public class LoginPageModel extends BasePageModel {

  /**
   * The URL for the page.
   */
  private static final String PAGE_URL = SeleniumConfig.getWebSiteBase() + "loginPage.html";

  /**
   * Username Field Selector.
   */
  private static final By USERNAME_INPUT = By.cssSelector("#UserName");

  /**
   * Password Field Selector.
   */
  private static final By PASSWORD_INPUT = By.cssSelector("#Password");

  /**
   * Login Error Message Selector.
   */
  private static final By LOGIN_ERROR = By.cssSelector("#LoginError");

  /**
   * Login Button Selector.
   */
  private static final By LOGIN_BUTTON = By.cssSelector("#Login");

  /**
   * The Selenium Test Object.
   */
  private SeleniumTestObject testObject;

  /**
   * Login Page Model Constructor.
   *
   * @param testObject
   *          The WebDriver object to use
   */
  public LoginPageModel(SeleniumTestObject testObject) {
    super(testObject);
  }

  /**
   * Open the login page.
   */
  public void openLoginPage() {
    this.testObject.getWebDriver().navigate().to(PAGE_URL);
    Assert.assertTrue(this.isPageLoaded(),
        String.format("The web page {0} is not loaded", PAGE_URL));
  }

  /**
   * Login with valid credentials.
   *
   * @param userName
   *          the user name
   * @param password
   *          the password
   * @return the home page model
   */
  public HomePageModel loginWithValidCredentials(String userName, String password) {
    this.enterCredentials(userName, password);
    this.testObject.getSeleniumWait().waitForElement(LOGIN_BUTTON);

    return new HomePageModel(this.testObject);
  }

  /**
   * Login with invalid credentials.
   *
   * @param userName
   *          the user name
   * @param password
   *          the password
   * @return the boolean
   */
  public boolean loginWithInvalidCredentials(String userName, String password) {
    this.enterCredentials(userName, password);
    this.testObject.getSeleniumWait().waitForElement(LOGIN_BUTTON).click();
    return this.testObject.getSeleniumWait().waitForElement(LOGIN_ERROR).isDisplayed();
  }

  /**
   * Enter credentials.
   *
   * @param userName
   *          the user name
   * @param password
   *          the password
   */
  public void enterCredentials(String userName, String password) {
    this.testObject.getSeleniumWait().waitForElement(USERNAME_INPUT).sendKeys(userName);
    this.testObject.getSeleniumWait().waitForElement(PASSWORD_INPUT).sendKeys(password);
  }

  public boolean isPageLoaded() {
    return this.testObject.getWebDriver().findElement(USERNAME_INPUT).isDisplayed()
        && this.testObject.getWebDriver().findElement(PASSWORD_INPUT).isDisplayed();
  }
}