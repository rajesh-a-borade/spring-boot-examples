package com.spring.rajesh.junitexamples.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.spring.rajesh.junitexamples.domain.Employee;
import com.spring.rajesh.junitexamples.domain.EmployeeView;
import com.spring.rajesh.junitexamples.repo.MyH2Repository;

@SpringBootTest
public class MyServiceTest {

	@MockBean
	private MyH2Repository myMockH2Repository;
	
	@BeforeEach
    void setup() {
        when(myMockH2Repository.findById(1)).thenReturn(new Employee(1, "ABC", 100));
    }
	
	@Autowired
	private MyService myService;
	
	@Test
	public void testService() {
		
		EmployeeView expectedEmpView = new EmployeeView(1, "ABC");
		EmployeeView empView = myService.findEmployeeViewById(1);
		assertEquals(expectedEmpView, empView);
	}
}
