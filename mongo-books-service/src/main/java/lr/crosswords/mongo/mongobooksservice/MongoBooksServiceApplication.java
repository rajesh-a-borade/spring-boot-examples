package lr.crosswords.mongo.mongobooksservice;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class MongoBooksServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoBooksServiceApplication.class, args);
	}

}

@RepositoryRestResource(collectionResourceRel = "crosswords", path = "books")
interface BooksRepository extends MongoRepository<Book, String> {

}


@Document
class Book {
	
	@Id
	private String storeBookId;
	
	private String bookId;
	
	private String authors;
	
	private String title;
	
	private String language;
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	//@Field("image_url")
	private String imageUrl;
	
	public Book() {
	}
	
	public Book(String storeBookId, String bookId, String authors, String title, String language, String imageUrl) {
		super();
		this.storeBookId = storeBookId;
		this.bookId = bookId;
		this.authors = authors;
		this.title = title;
		this.language = language;
		this.imageUrl = imageUrl;
	}

	public String getStoreBookId() {
		return storeBookId;
	}

	public void setStoreBookId(String storeBookId) {
		this.storeBookId = storeBookId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}

/*
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
		// this.booksRepository.flatMap(this.booksRepository::save);
		this.booksRepository.save(new Book("sbId1", "bId1", "Author", "Title", "Language", "Url"));
	}
}*/