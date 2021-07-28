package com.spring.rajesh.junitexamples.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.rajesh.junitexamples.domain.EmployeeView;
import com.spring.rajesh.junitexamples.service.MyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MyControllerStandaloneTest {

    private static final String GET_PATH = "/account";
    private MockMvc mvc;
    private MyController myController;
    private static ObjectMapper mapper = new ObjectMapper();

    @Mock
    private MyService myService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.myController = new MyController(myService);
        this.mvc = MockMvcBuilders.standaloneSetup(myController)
                .setControllerAdvice(MyControllerAdvice.class)
                .build();
    }

    @Test
    public void whenCorrectEmployeeId_thenReturn200() throws Exception {
        when(myService.findEmployeeViewById(1)).thenReturn(new EmployeeView(1, "XYZ"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(GET_PATH + "/1")
                .accept(MediaType.APPLICATION_JSON);
                //.param("id", "1");
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

}
