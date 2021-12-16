package br.com.erudio.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.security.AccountCredentialsVO;
import br.com.erudio.vo.EmbeddedResponseVO;
import br.com.erudio.vo.LoginResponseVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookControllerTest {

	private static final String HEADER_PARAM = "Authorization";
	private static final int SERVER_PORT = 8080;
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper = new ObjectMapper();
	private Long id = 0L;
	
	private BookVO book = new BookVO();

	@BeforeAll
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
	                	.as(LoginResponseVO.class)
	                .getToken();

	        specification =
	            new RequestSpecBuilder()
	                .addHeader(HEADER_PARAM, "Bearer " + token)
	                .setBasePath("/api/book/v1")
	                .setPort(SERVER_PORT)
	                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
	                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
	                .build();
	        
	        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	  
	@Test
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		
		book.setTitle("Docker Deep Dive");
		book.setAuthor("Nigel Poulton");
		book.setPrice(Double.valueOf(55.99));
		book.setLaunchDate(new Date());

		var content = given().spec(specification)
				.contentType("application/json")
					.body(book)
					.when()
					.post()
                .then()
            		.statusCode(200)
			            .extract()
			            .body()
			            	.asString();
		
		BookVO recorded = objectMapper.readValue(content, BookVO.class);
		id = recorded.getKey();
	}
	
	@Test
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType("application/json")
					.queryParams("page", 0 , "limit", 5, "direction", "asc")
					.when()
					.get()
                .then()
            		.statusCode(200)
			            .extract()
			            .body()
			            	.asString();
		
		EmbeddedResponseVO recorded = objectMapper.readValue(content, EmbeddedResponseVO.class);
		System.out.println(recorded);
	}

	//@Test
	public void testFindPersonByName() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		var content = given().spec(specification)
				.contentType("application/json")
					.pathParam("id", "23")
					.when()
					.get("{id}")
                .then()
            		.statusCode(200)
			            .extract()
			            .body()
			            	.asString();
		
		BookVO recorded = objectMapper.readValue(content, BookVO.class);
	}

	

	//@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDisablePerson() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

}
