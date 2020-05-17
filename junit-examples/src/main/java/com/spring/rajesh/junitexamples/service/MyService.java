package com.spring.rajesh.junitexamples.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rajesh.junitexamples.domain.Employee;
import com.spring.rajesh.junitexamples.domain.EmployeeView;
import com.spring.rajesh.junitexamples.repo.MyH2Repository;

@Service
public class MyService {

	@Autowired
	private MyH2Repository myH2Repository;

	public EmployeeView findEmployeeViewById(int id) {
		Employee emp = myH2Repository.findById(id);
		EmployeeView employeeView = new EmployeeView(emp.getId(), emp.getName());
		return employeeView;
	}

}
