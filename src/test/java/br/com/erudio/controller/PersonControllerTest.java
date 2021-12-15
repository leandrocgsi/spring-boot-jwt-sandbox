package br.com.erudio.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.security.AccountCredentialsVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestPropertySource("file:src/main/resources/application.yml")
public class PersonControllerTest {

	private Long id;
	public static final String HEADER_STRING = "Authorization";
	private PersonVO person;
	private RequestSpecification specification;

	@Value("${server.test-port}")
	private int portNumber;

	@BeforeClass
	public void authorization() {
		AccountCredentialsVO user = new AccountCredentialsVO();
		user.setUsername("leandro");
		user.setPassword("admin123");

		given()
			.basePath("/auth/signin")
			.port(portNumber)
			.contentType("application/json")
			.body(user)
			.when()
			.post()
			.then()
			.statusCode(200);

	    String token =
	            given()
	                //.basePath("/login")
	                .port(portNumber)
	                .contentType("application/json")
	                .body(user)
	                .when()
	                .post()
	                .then()
	                .statusCode(200)
	                .extract()
	                .header(HEADER_STRING);

	        specification =
	            new RequestSpecBuilder()
	                .addHeader(HEADER_STRING, token)
	                .setBasePath("/tasks")
	                .setPort(portNumber)
	                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
	                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
	                .build();
	}
	  
	@Test
	void testFindAll() {
		fail("Not yet implemented");
	}

	@Test
	void testFindPersonByName() {
		fail("Not yet implemented");
	}

	@Test
	void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	void testCreate() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	void testDisablePerson() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

}
