package com.spring.rajesh.junitexamples;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyH2Repository extends CrudRepository<Account, Long> {

	public Account findById(int i);
}
