package com.spring.rajesh.junitexamples;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MyControllerTest {

	@MockBean
	private MyService myMockService;
	
	@BeforeEach
	private void beforeAll() {
		when(myMockService.findAccountById(1)).thenReturn(new Account(1, "XYZ", 100));
	}
	
	
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testController() throws Exception {

        Account responseAccount = restTemplate.getForObject(
        		"http://localhost:" + port + "/account/1", Account.class);
        Account expectedAccount = new Account(1, "XYZ", 100);
        assertEquals(expectedAccount, responseAccount);
        System.out.println("*** TEST - MyControllerTest passed");
    }
}