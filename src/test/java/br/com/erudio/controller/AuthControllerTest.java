package br.com.erudio.controller;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.erudio.security.AccountCredentialsVO;
import br.com.erudio.vo.ResponseVO;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerTest {

	public static final String HEADER_STRING = "Authorization";
	public static final int SERVER_PORT = 8888;
	
	@Test
	void testSignin() {
		AccountCredentialsVO user = new AccountCredentialsVO();
		user.setUsername("leandro");
		user.setPassword("admin123");

	    var token =
	            given()
	                .basePath("/auth/signin")
	                .port(SERVER_PORT)
	                .contentType("application/json")
	                .body(user)
	                .when()
	                	.post()
	                .then()
	                	.statusCode(200)
	                .extract()
	                .body()
	                	.as(ResponseVO.class)
	                .getToken();
	    
	    System.out.println(token);
	}

}
