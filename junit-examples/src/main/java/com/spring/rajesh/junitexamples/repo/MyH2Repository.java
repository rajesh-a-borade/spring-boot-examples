package com.spring.rajesh.junitexamples.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring.rajesh.junitexamples.domain.Employee;

@Repository
public interface MyH2Repository extends CrudRepository<Employee, Long> {

	public Employee findById(int i);
}
