package com.spring.rajesh.junitexamples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {

	@Autowired
	private MyH2Repository myH2Repository;
	
	public Account findAccountById(int id) {
		return myH2Repository.findById(id);
	}

	
}
