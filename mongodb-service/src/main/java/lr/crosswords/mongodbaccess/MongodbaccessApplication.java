package lr.crosswords.mongodbaccess;


import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@EnableEurekaClient
@SpringBootApplication
public class MongodbaccessApplication {

	@Bean
	RouterFunction<ServerResponse> routes(BooksRepository booksRepository) {
		return RouterFunctions.route(RequestPredicates.GET("/books"), new HandlerFunction<ServerResponse>() {

			@Override
			public Mono<ServerResponse> handle(ServerRequest request) {
				
				return ServerResponse.ok().body(booksRepository.findAll(), Book.class);
			}
		});
	}
	
	
	private static final Logger LOG = Logger.getLogger(MongodbaccessApplication.class.getName());
	
	@Autowired
	private BooksRepository booksRepository;
	
	@RequestMapping("/oldway/books")
	public @ResponseBody Flux<Book> getBooks() {
		LOG.info("Mongo DB service /oldway/books called ... ");
		return booksRepository.findAll();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MongodbaccessApplication.class, args);
	}

}


@Component
class MongoDbInitializer implements ApplicationRunner {

	private final BooksRepository booksRepository;
	
	public MongoDbInitializer(BooksRepository booksRepository) {
		this.booksRepository = booksRepository;
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		
		// Flux<String> names = Flux.just("Book 1", "Book 2", "Book 3");
		// Flux<Book> books = names.map(name -> new Book(null, name));
		
		this.booksRepository.deleteAll()
							.thenMany(Flux.just("Book 1", "Book 2", "Book 3"))
							.map(name -> new Book(null, name))
							.flatMap(this.booksRepository::save)
							.thenMany(this.booksRepository.findAll())
							.subscribe(System.out::println);
	}
}

interface BooksRepository extends ReactiveMongoRepository<Book, String> {
	
}

@Document
class Book {
	@Id
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
