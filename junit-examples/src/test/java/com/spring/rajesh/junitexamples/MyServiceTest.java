package com.spring.rajesh.junitexamples;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
public class MyServiceTest {

	@MockBean
	private MyH2Repository myMockH2Repository;
	
	@BeforeEach
    void setMockOutput() {
        when(myMockH2Repository.findById(1)).thenReturn(new Account(1, "ABC", 100));
    }
	
	@Autowired
	private MyService myService;
	
	@Test
	public void testService() {
		
		Account account = new Account(1, "ABC", 100);
		Account accountFoudFromService = myService.findAccountById(1);
		assertEquals(account, accountFoudFromService);
		System.out.println("*** TEST - MyServiceTest passed");
	}
}
