package com.spring.rajesh.junitexamples.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.spring.rajesh.junitexamples.domain.Employee;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MyRepositoryTest {

	@Autowired 
	private MyH2Repository myH2Repository;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testCrud() {
		
		Employee emp = new Employee();
		emp.setName("ABC");
		emp.setSalary(9999);
		myH2Repository.save(emp);
		Employee empFromRepo = myH2Repository.findById(1);
		assertEquals(emp, empFromRepo);
	}
}
