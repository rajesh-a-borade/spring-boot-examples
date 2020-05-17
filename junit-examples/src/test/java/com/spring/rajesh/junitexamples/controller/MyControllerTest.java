package com.spring.rajesh.junitexamples.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.spring.rajesh.junitexamples.domain.EmployeeView;
import com.spring.rajesh.junitexamples.service.MyService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MyControllerTest {

	@MockBean
	private MyService myMockService;
	
	@BeforeEach
	private void beforeAll() {
		when(myMockService.findEmployeeViewById(1)).thenReturn(new EmployeeView(1, "XYZ"));
	}
	
	
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testController() throws Exception {

    	EmployeeView responseEmployeeView = restTemplate.getForObject(
        		"http://localhost:" + port + "/account/1", EmployeeView.class);
    	EmployeeView expectedEmployeeView = new EmployeeView(1, "XYZ");
        assertEquals(expectedEmployeeView, responseEmployeeView);
    }
}