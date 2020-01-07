package lr.rajesh.h2sqlservice;

import java.util.Collection;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@EnableDiscoveryClient
@SpringBootApplication
public class H2sqlServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(H2sqlServiceApplication.class, args);
	}

}


@Entity
class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    private String reservationName;

    public Long getId() {
        return id;
    }

    public Reservation() {
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationName='" + reservationName + '\'' +
                '}';
    }

    public Reservation(String reservationName) {
        this.reservationName = reservationName;
    }

    public String getReservationName() {
        return reservationName;
    }
}


@Component
class SampleDataCLR implements CommandLineRunner {

    private final ReservationRepository reservationRepository;

    @Autowired
    public SampleDataCLR(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Stream.of("Josh", "Dr. Johnson", "Dr. Syer", "Dr. Pollack")
                .forEach(n -> reservationRepository.save(new Reservation(n)));
        reservationRepository.findAll().forEach(System.out::println);
    }
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @RestResource(path = "by-name")
    Collection<Reservation> findByReservationName(@Param("rn") String rn);
}



@RefreshScope
@RestController
class MessageRestController {

    private final String value;

    @Autowired
    public MessageRestController(@Value("${message}") String value) {
        this.value = value;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/message")
    String read() {
        return this.value;
    }
}
