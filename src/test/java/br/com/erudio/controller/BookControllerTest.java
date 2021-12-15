package br.com.erudio.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.security.AccountCredentialsVO;
import br.com.erudio.vo.ResponseVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookControllerTest {

	private static final String HEADER_PARAM = "Authorization";
	private static final int SERVER_PORT = 8888;
	private static RequestSpecification specification;
	
	private BookVO book = new BookVO();

	@BeforeClass
	public static void authorization() {
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

	        specification =
	            new RequestSpecBuilder()
	                .addHeader(HEADER_PARAM, token)
	                .setBasePath("/api/book/v1")
	                .setPort(SERVER_PORT)
	                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
	                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
	                .build();
	}
	  
	@Test
	public void testFindAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindPersonByName() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreate() {
		
		book.setTitle("Docker Deep Dive");
		book.setAuthor("Nigel Poulton");
		book.setPrice(Double.valueOf(55.99));
		book.setLaunchDate(new Date());
		
		var foo = given().spec(specification).contentType("application/json").body(book).when().post();
		System.out.println(foo);
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testDisablePerson() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

}
