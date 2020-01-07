package com.lr.springuiservice;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Predicate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringUiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringUiServiceApplication.class, args);
	}
	
	@Bean
	// @LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> loggingFilter(){
	    FilterRegistrationBean<AuthenticationFilter> registrationBean 
	      = new FilterRegistrationBean<>();
	         
	    registrationBean.setFilter(new AuthenticationFilter());
	    registrationBean.addUrlPatterns("/secure/*");
	         
	    return registrationBean;    
	}

}

@RestController
class AuthenticationController {
	
	private final RestTemplate restTemplate;
	
	@Autowired
	public AuthenticationController(RestTemplate restTemplate) {
		
		this.restTemplate = restTemplate;
	}
	
	@PostMapping("/api/login")
	public ResponseEntity<ServerResponse> login(@RequestBody Credentials credentials, HttpServletRequest request,
			HttpServletResponse response) {
		
		// validate input
		ResponseEntity<ServerResponse> responseEntity;
		
		boolean isValid = _isValid(credentials);
		if(isValid) {
			
			HttpSession httpSession = request.getSession(true);
			String jSessionId = httpSession.getId();
			String redisSessionId = _createRedisSession(jSessionId);
			httpSession.setAttribute(jSessionId, redisSessionId);
			ServerResponse serverResponse = new ServerResponse(OperationStatus.SUCCESS);
			responseEntity = new ResponseEntity<>(serverResponse, HttpStatus.OK);
			return responseEntity;
		}
		ServerResponse serverResponse = new ServerResponse(OperationStatus.FAILED);
		responseEntity = new ResponseEntity<>(serverResponse, HttpStatus.UNAUTHORIZED);
		return responseEntity;
	}
	
	private String _createRedisSession(String jSessionId) {
		
		try {
			UUID uuid = UUID.randomUUID();
			String uuidStr = uuid.toString();
			Session session = new Session(jSessionId, uuidStr);
			HttpEntity<Session> request = new HttpEntity<>(session);
			ResponseEntity<Object> responseEntity = restTemplate.postForEntity("http://localhost:4444/sessions", request, Object.class);
			if(responseEntity.getStatusCodeValue() == 201) {
				return uuidStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean _isValid(Credentials credentials) {
		if(1==1) {
			return true;
		}
		try {
			HttpEntity<Credentials> request = new HttpEntity<>(credentials);
			ServerResponse serverResponse = this.restTemplate.postForObject("http://localhost:4444/sessions", request, ServerResponse.class);
			if(serverResponse != null) {
				if(serverResponse.getStatus().equals(OperationStatus.SUCCESS)) {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	@GetMapping("/secure/data")
	public ResponseEntity<Data> getData() {
		return new ResponseEntity<>(new Data("data from server"), HttpStatus.OK);
	}
	
}

class Data {
	
	public String message;
	
	public Data() {
	}
	
	public Data(String message) {
		super();
		this.message = message;
	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}



class Credentials {
	
	private String email;
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}

class ServerResponse {
	
	private String status;
	
	public ServerResponse() {
	}
	
	public ServerResponse(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}

class OperationStatus {
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	
	private OperationStatus() {}
}


class SessionCookiePredicate {
	
	public Predicate<Cookie> isSessionCookie() {
		return p -> p.getName().equals("SESSIONID");
	}
}


class Session implements Serializable {
	
	private static final long serialVersionUID = 4472229046079733833L;
	
	private String sessionKeyId;
	private String sessionValue;
	
	public Session(String sessionKeyId, String sessionValue) {
		super();
		this.sessionKeyId = sessionKeyId;
		this.sessionValue = sessionValue;
	}
	public String getSessionKeyId() {
		return sessionKeyId;
	}
	public void setSessionKeyId(String sessionKeyId) {
		this.sessionKeyId = sessionKeyId;
	}
	public String getSessionValue() {
		return sessionValue;
	}
	public void setSessionValue(String sessionValue) {
		this.sessionValue = sessionValue;
	}
	@Override
	public String toString() {
		return "Session [sessionKeyId=" + sessionKeyId + ", sessionValue=" + sessionValue + "]";
	}
	
}

class RedisSessionResponse {
	
	private String sessionValue;
	private Object _links;
	
	public String getSessionValue() {
		return sessionValue;
	}
	public void setSessionValue(String sessionValue) {
		this.sessionValue = sessionValue;
	}
	public Object get_links() {
		return _links;
	}
	public void set_links(Object _links) {
		this._links = _links;
	}
}
