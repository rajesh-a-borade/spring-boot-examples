package lr.redis.sessionmgmt;

import java.io.Serializable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@EnableEurekaClient
@SpringBootApplication
public class SessionMgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SessionMgmtApplication.class, args);
	}

}

//
//@RestController
//@RequestMapping("/sessions")
//class SessionController {
//	
//	@Autowired
//	private SessionRepository sessionRepository;
//	
//	@RequestMapping("/all")
//	public List<Session> getAllSession() {
//		sessionRepository.save(new Session("id001", "v001"));
//		sessionRepository.save(new Session("id002", "v002"));
//		List<Session> sessions = new ArrayList<>();
//		sessionRepository.findAll().forEach(sessions::add);
//		return sessions;
//	}
//}

@Configuration
class RedisSessionHandler {
	
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
	    return new JedisConnectionFactory();
	}
	 
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
	    RedisTemplate<String, Object> template = new RedisTemplate<>();
	    template.setConnectionFactory(jedisConnectionFactory());
	    return template;
	}
}


@RedisHash("Session")
class Session implements Serializable {
	
	private static final long serialVersionUID = 4472229046079733833L;
	
	@Id
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


@RepositoryRestResource
interface SessionRepository extends CrudRepository<Session, String> {}