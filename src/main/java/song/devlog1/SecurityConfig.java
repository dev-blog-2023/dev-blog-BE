package song.devlog1;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import song.devlog1.repository.UserJpaRepository;
import song.devlog1.security.filter.JwtAuthenticationFilter;
import song.devlog1.security.filter.LogFilter;
import song.devlog1.security.authentication.*;
import song.devlog1.entity.role.RoleName;
import song.devlog1.security.oauth2.NaverOAuth2UserService;
import song.devlog1.security.userdetails.UserDetailsServiceImpl;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserJpaRepository userRepository;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final ObjectMapper objectMapper;
    private final NaverOAuth2UserService naverOAuth2UserService;

    private final String NAVER = "naver";
//    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
//    private String client_id;
//    @Value("${spring.security.oauth2.client.registration.naver.client-secrete}")
//    private String client_secrete;

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/", "/login", "logout", "/signup", "/css/**", "/*.ico", "/*.js", "/error").permitAll()
                        .requestMatchers(regexMatcher("^/board/[0-9]+$")).permitAll()
                        .requestMatchers("/board/**").authenticated()
                        .requestMatchers("/comment/**").authenticated()
                        .requestMatchers("/admin/**").hasAuthority(RoleName.ROLE_ADMIN.name())
                        .anyRequest().permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint(objectMapper))
                        .accessDeniedHandler(accessDeniedHandler(objectMapper))
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
//                .oauth2Login(withDefaults())
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login/oauth2")
//                        .authorizationEndpoint(authorizationendpoint->authorizationendpoint
//                                .baseUri("https://nid.naver.com/oauth2.0/authorize"))
//                        .redirectionEndpoint(redirectEndpoint->redirectEndpoint
//                                .baseUri("/login/oauth2/naver"))
//                        .authorizationEndpoint(authorization -> authorization
//                                .baseUri("/oauth2/authorize"))
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(naverOAuth2UserService))
//                        .clientRegistrationRepository(new InMemoryClientRegistrationRepository())
//                        .authorizedClientRepository()
//                        .successHandler(null)
//                        .failureHandler(null))
//                )
                .addFilterAfter(new JwtAuthenticationFilter(userDetailsService(userRepository), authenticationEntryPoint(objectMapper)), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new LogFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserJpaRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return new AuthenticationHandler(objectMapper);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return new AuthorizationHandler(objectMapper);
    }

}
