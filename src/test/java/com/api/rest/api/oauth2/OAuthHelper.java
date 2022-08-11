package com.api.rest.api.oauth2;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OAuthHelper {
	/***
	 * --------------------- Design ------------------------	
	 * 1. ChromeWebDriver -> The responsibility is to create the instance of chrome browse
	 * 2. OAuthHelper ->
	 * 	a. Call the ChromeWebDriver to get the instance of chrome browser.
	 * 	b. Send the request to authorize end point and extract the code.
	 * 3. TestGetListofFile
	 * */

	private WebDriver driver;
	private WebDriverWait wait;
	
	private By login_email = By.name("login_email");
	private By login_password = By.name("login_password");
	private By submit = By.xpath("//button[@type='submit']");
	
	/*
	private By login_by_gmail = By.className("auth-google");
	private By login_email = By.xpath("//*[@id=\"identifierId\"]");
	private By login_email_next_button = By.className("VfPpkd-LgbsSe-OWXEXe-k8QpJ");
	private By login_password = By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input");
	private By login_password_next_button = By.xpath("//*[@id=\"passwordNext\"]/div/button/div[3]");
	private By continuebutton = By.xpath("//*[@id=\"warning-button-continue\"]");
	private By allowbutton = By.xpath("//*[@id=\"buttons\"]/div/form[2]/button");
	*/
	
	public OAuthHelper() {
		driver = ChromeWebDriver.getChromeDriver();
		wait = new WebDriverWait(driver, 3000);
	}

	private String getBaseUrl(String client_id, String redirect_uri) {
		String url = String.format(
				"https://www.dropbox.com/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=%s", client_id,
				redirect_uri);
		System.out.println("Base Url " + url);
		return url;

	}

	/*private void login(String username, String password) {
         driver.findElement(login_by_gmail).click();
         driver.findElement(login_email).sendKeys(username);
         driver.findElement(login_email_next_button).click();
	}*/
	
	private void login(String username, String password) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(submit));
		driver.findElement(login_email).sendKeys(username);
		driver.findElement(login_password).sendKeys(password);
		driver.findElement(submit).click();
		wait.until(ExpectedConditions.urlContains("?code="));

	}

	private void setup() {
		driver.manage().timeouts().pageLoadTimeout(30000, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20000, TimeUnit.SECONDS);
		driver.manage().window().maximize();

	}

	public String getOauthToken(String username, String password, String client_id, String client_secret,
			String redirect_uri) {
		String currUrl = "";
		try {
			setup();
			String baseUrl = getBaseUrl(client_id, redirect_uri);
			driver.navigate().to(baseUrl);
			login(username, password);
			currUrl = driver.getCurrentUrl();
		} finally {
			if(driver != null)
				driver.quit();
		}
		return parseToken(currUrl);
	}

	private String parseToken(String code) {
		String authcode = "";
		if(code.contains("=")) {
			authcode = code.split("=")[1];
		}
		System.out.println("Auth Code " + authcode);
		return authcode;
	}

}
