package lr.security.api.jwt;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootApplication
public class JwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

}


@Entity
@Table(name="vehicles")
class Vehicle implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id ;
	@Column
	private String name;
	
	public Vehicle(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Vehicle() {
	}

	public Vehicle(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Vehicle [id=" + id + ", name=" + name + "]";
	}
	
	
}

class VehicleForm {
    
	private String name;
    
    public VehicleForm() {
	}

	public VehicleForm(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}

@RepositoryRestResource(path = "vehicles", collectionResourceRel = "vehicles", itemResourceRel = "vehicle")
interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}

@RestController
@RequestMapping("/v1/vehicles")
class VehicleController {
	
    private VehicleRepository vehicles;
    public VehicleController(VehicleRepository vehicles) {
        this.vehicles = vehicles;
    }
    @GetMapping("")
    public ResponseEntity all() {
        return ok(this.vehicles.findAll());
    }
    @PostMapping("")
    public ResponseEntity save(@RequestBody VehicleForm form, HttpServletRequest request) {
        Vehicle saved = this.vehicles.save(new Vehicle(form.getName()));
        return created(
            ServletUriComponentsBuilder
                .fromContextPath(request)
                .path("/v1/vehicles/{id}")
                .buildAndExpand(saved.getId())
                .toUri())
            .build();
    }
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) throws Exception {
        return ok(this.vehicles.findById(id).orElseThrow(() -> new Exception("Vehicle not found")));
    }
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody VehicleForm form) throws Exception {
        Vehicle existed = this.vehicles.findById(id).orElseThrow(() -> new Exception("Vehicle not found"));
        existed.setName(form.getName());
        this.vehicles.save(existed);
        return noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) throws Exception {
        Vehicle existed = this.vehicles.findById(id).orElseThrow(() -> new Exception("Vehicle not found"));
        this.vehicles.delete(existed);
        return noContent().build();
    }
}


@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public String vehicleNotFound(Exception ex, WebRequest request) {
        System.out.println("handling VehicleNotFoundException...");
        return "EXCEPTION";
    }
}

@Component
class CarDataInitializer implements CommandLineRunner {
    @Autowired
    VehicleRepository vehicles;
    @Override
    public void run(String... args) throws Exception {
        Arrays.asList("moto", "car").forEach(v -> this.vehicles.saveAndFlush(new Vehicle(v)));
        this.vehicles.findAll().forEach(System.out::println);
    }
}

class JwtTokenFilter extends GenericFilterBean {
    private JwtTokenProvider jwtTokenProvider;
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
        throws IOException, ServletException {
    	try {
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = token != null ? jwtTokenProvider.getAuthentication(token) : null;
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(req, res);			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}


@Component
class JwtTokenProvider {
	
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h
    @Autowired
    private UserDetailsService userDetailsService;
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()//
            .setClaims(claims)//
            .setIssuedAt(now)//
            .setExpiration(validity)//
            .signWith(SignatureAlgorithm.HS256, secretKey)//
            .compact();
    }
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
    public boolean validateToken(String token) throws Exception {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new Exception("Expired or invalid JWT token");
        }
    }
}


class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    
	private JwtTokenProvider jwtTokenProvider;
    
    public JwtConfigurer(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}


@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers("/auth/signin").permitAll()
                .antMatchers(HttpMethod.GET, "/vehicles/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/vehicles/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/v1/vehicles/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .apply(new JwtConfigurer(jwtTokenProvider));
        //@formatter:on
    }
}

@Entity
@Table(name="users")
class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    
    private String username;
    
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();
    
    public User() {
	}
    
    public User(String username, String password, List<String> roles) {
		super();
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", roles=" + roles + "]";
	}
    
    
}

interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

@Component
class CustomUserDetailsService implements UserDetailsService {
    
	private UserRepository users;
    
    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.users.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }
}

@RestController()
class UserinfoController {
    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails){
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
            .stream()
            .map(a -> ((GrantedAuthority) a).getAuthority())
            .collect(Collectors.toList())
        );
        return ok(model);
    }
}

@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository users;
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {
        try {
            String username = data.getUsername();
            // validate username and password
            String token = jwtTokenProvider.createToken(username, Stream.of("ROLE_ADMIN","ROLE_USER").collect(Collectors.toList()));
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}

class AuthenticationRequest implements Serializable {
 
	private static final long serialVersionUID = -6986746375915710855L;
	private String username;
    private String password;
    
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}

@Component
class DataInitializer implements CommandLineRunner {
	//...
    @Autowired
    UserRepository users;
    @Override
    public void run(String... args) throws Exception {
        
        User user = new User("user", "password", Arrays.asList("ROLE_USER"));
        this.users.save(user);
        User admin = new User("admin", "password", Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        this.users.save(admin);
        
        this.users.findAll().forEach(System.out::println);
    }
}