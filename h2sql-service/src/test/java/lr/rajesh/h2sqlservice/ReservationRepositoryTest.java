package lr.rajesh.h2sqlservice;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class ReservationRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private ReservationRepository reservationRepository;
    
    
    @Test
    public void whenfindByReservationName_thenReturnReservation() {
        // given
    	Reservation createdReservation = new Reservation("alex");
        entityManager.persist(createdReservation);
        entityManager.flush();
     
        // when
        Optional<Reservation> found = reservationRepository.findByReservationName(createdReservation.getReservationName()).stream().findAny();
     
        Reservation reservationFound = found.get();
        
        // then
        assertThat(reservationFound.getReservationName()).isEqualTo(createdReservation.getReservationName());
    }
}
