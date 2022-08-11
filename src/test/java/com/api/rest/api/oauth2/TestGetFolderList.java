package com.api.rest.api.oauth2;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class TestGetFolderList {

	private static String code = "";
	private static String token = "";

	private static String client_id = "cu3jjne2nn6imdk";
			//"pmox2zjdjilv7uh";
	private static String client_secret = "l79bbvoohxrqkjx";
			//"e227wz6oszlrahq";

	private static String username = "jonathan.regan58@yahoo.com";
			//"jonathanregan58@gmail.com";
	private static String password = "Abcdef12345";
	//"DasJetDog58@_";
	private static String redirect_uri = "https://www.google.com/";
    private String token2 = "Y3UzampuZTJubjZpbWRrOmw3OWJidm9vaHhycWtqeA==";
	@BeforeClass
	public static void name() {
		code = new OAuthHelper().getOauthToken(username, password, client_id, client_secret, redirect_uri);
		token = given().contentType(ContentType.URLENC).formParam("code", code)
				.formParam("grant_type", "authorization_code").formParam("redirect_uri", redirect_uri)
				.formParam("client_id", client_id).formParam("client_secret", client_secret).when()
				.post("https://api.dropbox.com/oauth2/token").then().assertThat().statusCode(HttpStatus.SC_OK).extract()
				.jsonPath().get("access_token");
	}

	@Test
	public void testGetFolderList() {
		
		String response = given().auth().oauth2(token2).contentType(ContentType.JSON)
				//(token).contentType(ContentType.JSON)
				//"{\"query\":\"foo\"}"
				.body("{\"query\":\"foo\"}").when()
				//.body("{\"path\": \"\",\"recursive\": false}").when()
				.post("https://api.dropboxapi.com/2/check/app").then().assertThat().statusCode(HttpStatus.SC_OK)
				//.post("https://api.dropboxapi.com/2/files/list_folder").then().assertThat().statusCode(HttpStatus.SC_OK)
				.extract().asString();
		System.out.println(response);

	}

}
