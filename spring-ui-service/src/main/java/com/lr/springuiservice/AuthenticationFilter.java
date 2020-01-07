package com.lr.springuiservice;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		System.out.println("~~~AuthenticationFilter~~~");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession httpSession = request.getSession(false);
		Cookie cookies[] = request.getCookies();
		
		String[] allowDomain = {"http://localhost:3000","https://localhost:8080"};
	    Set<String> allowedOrigins = new HashSet<String>(Arrays.asList (allowDomain));                  
	    String originHeader = request.getHeader("Origin");
		response.setHeader("Access-Control-Allow-Origin", originHeader);
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
	    
		System.out.println("--- cookies ---");
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				System.out.println(cookie.getName() + "=" + cookie.getValue());
			}
		}
		System.out.println("------");
		
		if(httpSession != null) {
			
			Optional<Cookie> sessionCookie = Stream.of(cookies)
				.filter(p -> p.getName().equals("JSESSIONID"))
				.findAny();
			
			if(sessionCookie.isPresent()) {
				//if(sessionCookie.get().equals("qwertyuiop")) {
					// talk to session management service
				String jSessionId = sessionCookie.get().getValue();
				String rSessionId = httpSession.getAttribute(jSessionId).toString();
				System.out.println("Session attribute redisSessionId = " + rSessionId);
				System.out.println("------");
				
				if(_isValidRSession(jSessionId, rSessionId)) {
					chain.doFilter(request, response);
				}
				//}
			}
		}
		else {
			// chain.doFilter(request, response);
			response.sendError(401);			
		}
	}

	@Override
	public void destroy() {
		
	}
	
	private boolean _isValidRSession(String jSessionId, String rSessionId) {
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<RedisSessionResponse> responseEntity = restTemplate.getForEntity("http://localhost:4444/sessions/"+jSessionId, RedisSessionResponse.class);
		RedisSessionResponse session = responseEntity.getBody();
		System.out.println("---");
		System.out.println(session);
		System.out.println("---");
		if(session.getSessionValue().equals(rSessionId)) {
			return true;
		}
		return false;
	}

}
