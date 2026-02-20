package fit.workout_tracker.api.config;

import fit.workout_tracker.api.filter.JwtAuthenticationFilter;
import fit.workout_tracker.api.repository.UserRepository;
import fit.workout_tracker.api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;

    @Autowired
    public SecurityConfig(
            UserRepository userRepository,
            HandlerExceptionResolver handlerExceptionResolver,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(c ->
                c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.cors(corsConfig
                -> corsConfig.configurationSource(corsConfigurationSource()));

        http.authorizeHttpRequests(c -> {
           c.requestMatchers(
                   "/h2-console/**",
                   "/auth/*"
           ).permitAll();
           c.anyRequest().authenticated();
        });

        http.addFilterBefore(
                jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
        );

        http.headers(headerCustomizer ->
                headerCustomizer.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userEmail -> userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> UsernameNotFoundException.fromUsername(userEmail));
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of(
                "GET","POST","PUT","DELETE","PATCH"
        ));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/h2-console/**", configuration);
        return source;
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
                handlerExceptionResolver,
                jwtService,
                userDetailsService()
        );
    }
}
