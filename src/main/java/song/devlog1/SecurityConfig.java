package song.devlog1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import song.devlog1.security.authentication.AuthenticationHandler;
import song.devlog1.security.authentication.JwtAuthenticationFilter;
import song.devlog1.security.authentication.LoginFailureHandler;
import song.devlog1.security.authentication.LoginSuccessHandler;
import song.devlog1.entity.role.RoleName;
import song.devlog1.security.oauth2.NaverOAuth2UserService;

import static org.springframework.security.config.Customizer.*;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationHandler authenticationHandler;
    private final NaverOAuth2UserService naverOAuth2UserService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
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
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(authenticationHandler);
                })
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(new InMemoryClientRegistrationRepository())
                        .authorizationEndpoint(authorizationendpoint->authorizationendpoint
                                .baseUri("https://nid.naver.com/oauth2.0/authorize"))
                        .redirectionEndpoint(redirectEndpoint->redirectEndpoint
                                .baseUri("/login/oauth2/{registrationId}"))
                        .loginPage("/oauth2/authorization/naver")
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorize"))
                        .redirectionEndpoint(redirectEndpoint -> redirectEndpoint
                                .baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(naverOAuth2UserService))
                        .authorizedClientRepository()
                        .successHandler(null)
                        .failureHandler(null))
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
