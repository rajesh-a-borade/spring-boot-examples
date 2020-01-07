package lr.crosswords.gateway.apigateway;


import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@EnableEurekaClient
@EnableZuulProxy
@SpringBootApplication
// @EnableRedisHttpSession
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

}

@RestController
class BooksController {
	
	private final RestTemplate restTemplate;
	
	@Autowired
	public BooksController(RestTemplate restTemplate) {
		
		this.restTemplate = restTemplate;
	}
	
	private static final Logger LOG = Logger.getLogger(BooksController.class.getName());
	
	@RequestMapping(value = "/admin")
	public @ResponseBody String getAdmin() {
		return "allowed";
	}
	
	@RequestMapping(value = "/api/books")
	public @ResponseBody List<String> getAllBooks() {
		
		LOG.info("APIGateway service /api/books called ... ");
		
		ResponseEntity<List<Book>> response = this.restTemplate.exchange(
				"http://mongodb-service/oldway/books",
				  HttpMethod.GET,
				  null,
				  new ParameterizedTypeReference<List<Book>>(){});
		
		List<String> names = response.getBody().stream().map(book -> book.getName()).collect(Collectors.toList());
		
		return names;
	}
}

class Book {

	private String id;
	private String name;
	
	public Book() {
	}
	
	public Book(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + "]";
	}
	
	
}


@EnableWebSecurity
@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
	    auth.inMemoryAuthentication()
	        .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
	        .and()
	        .withUser("user").password(encoder().encode("userPass")).roles("USER");
	}
	 
	@Bean
	public PasswordEncoder  encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Autowired
	private MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;
	
	@Autowired
	private MyFailureHandler myFailureHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { 
	    http
	    .csrf().disable()
	    .exceptionHandling()
	    //.authenticationEntryPoint(restAuthenticationEntryPoint)
	    .and()
	    .authorizeRequests()
	    .antMatchers("/api/books").authenticated()
	    .antMatchers("/admin/**").hasRole("ADMIN")
	    .and()
	    .formLogin()
	    .successHandler(mySuccessHandler)
	    .failureHandler(myFailureHandler)
	    .and()
	    .logout();
	}
}

