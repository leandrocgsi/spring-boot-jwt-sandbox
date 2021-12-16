package br.com.erudio.controller;

import static io.restassured.RestAssured.given;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerTest {

	private static final String HEADER_PARAM = "Authorization";
	private static final int SERVER_PORT = 8080;
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static BookVO book;

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
	        
	        objectMapper = new ObjectMapper();
	        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        
	        book = new BookVO();
	}
	  
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		
		mockBook();

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
		
		book = objectMapper.readValue(content, BookVO.class);
		System.out.println(book.getTitle());
	}
	  
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		
		book.setTitle("Docker Deep Dive - Updated");

		var content = given().spec(specification)
				.contentType("application/json")
					.body(book)
					.when()
					.put()
                .then()
            		.statusCode(200)
			            .extract()
			            .body()
			            	.asString();
		
		book = objectMapper.readValue(content, BookVO.class);
		System.out.println(book.getTitle());
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		var content = given().spec(specification)
				.contentType("application/json")
					.pathParam("id", book.getKey())
					.when()
					.get("{id}")
                .then()
            		.statusCode(200)
			            .extract()
			            .body()
			            	.asString();
		
		BookVO recorded = objectMapper.readValue(content, BookVO.class);
	}
	
	@Test
	@Order(4)
	public void testDelete() {
		var content = given().spec(specification)
				.contentType("application/json")
					.pathParam("id", book.getKey())
					.when()
					.delete("{id}")
                .then()
            		.statusCode(204);
	}
		
	private void mockBook() {
		book.setTitle("Docker Deep Dive");
		book.setAuthor("Nigel Poulton");
		book.setPrice(Double.valueOf(55.99));
		book.setLaunchDate(new Date());
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

	

}
