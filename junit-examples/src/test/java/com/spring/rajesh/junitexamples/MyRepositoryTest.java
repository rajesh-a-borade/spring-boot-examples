package com.spring.rajesh.junitexamples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MyRepositoryTest {

	@Autowired
	private MyH2Repository myH2Repository;
	
	public void testCrud() {
		
		Account account = new Account(100, "XYZ", 1000);
		myH2Repository.save(account);
		Account fromRepoAccount = myH2Repository.findById(100);
		assertEquals(account, fromRepoAccount);
		System.out.println("*** TEST - MyRepositoryTest passed");
	}
}
