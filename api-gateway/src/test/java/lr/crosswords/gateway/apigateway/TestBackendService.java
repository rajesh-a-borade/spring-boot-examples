package lr.crosswords.gateway.apigateway;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestBackendService {
	
	@Test
	public void test() {
		
		TestRestTemplate testRestTemplate = new TestRestTemplate();
		ResponseEntity<List<Book>> response = testRestTemplate.exchange("http://localhost:7777/oldway/books",
				  HttpMethod.GET,
				  null,
				  new ParameterizedTypeReference<List<Book>>(){});
		List<String> bookNames = response.getBody().stream().map(book -> book.getName()).collect(Collectors.toList());
		System.out.println("~~~ bookNames" + bookNames);
		String[] books = {"Book 1", "Book 2", "Book 3"};  
		List<String> expected = Arrays.asList(books);
		System.out.println("~~~ expected" + expected);
		Assertions.assertThat(bookNames.equals(expected));
	}
}
