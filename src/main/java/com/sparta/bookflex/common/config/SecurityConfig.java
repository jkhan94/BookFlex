package com.sparta.bookflex.common.config;


import com.sparta.bookflex.common.jwt.JwtProvider;
import com.sparta.bookflex.common.security.AuthenticationEntryPointImpl;
import com.sparta.bookflex.common.security.JwtAuthenticationFilter;
import com.sparta.bookflex.common.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(jwtProvider, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        http.sessionManagement(
            (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(
            (authorizationHttpRequests) -> authorizationHttpRequests
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                .requestMatchers(HttpMethod.GET,"/api/users/all").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/users/{userId}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/users/{userId}/state").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/users/{userId}/grade").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.GET,"/api/sales/admin").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.DELETE,"/api/qnas/admin/{qnaId}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/qnas/admin/{qnaId}").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.POST,"/api/coupons").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/coupons/{couponId}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/coupons/issue/all/{couponId}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/coupons/{couponId}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/coupons/{couponId}").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.POST,"/api/books").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/books/{booksId}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/books/{booksId}").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.DELETE,"/api/reviews/{reviewId}").hasAuthority("ADMIN")

                .anyRequest().authenticated()
        );

        http.exceptionHandling((e) ->
            e.authenticationEntryPoint(authenticationEntryPoint));
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}