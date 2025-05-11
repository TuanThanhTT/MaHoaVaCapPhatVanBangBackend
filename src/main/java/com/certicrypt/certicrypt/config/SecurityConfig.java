package com.certicrypt.certicrypt.config;

import com.certicrypt.certicrypt.service.CustomUserDetailsService;
import com.certicrypt.certicrypt.util.JwtAuthenticationFilter;
import com.certicrypt.certicrypt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoint công khai
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/degree/export/check").permitAll()
                        .requestMatchers("/admin/query/all").permitAll()
                        .requestMatchers("/admin/Status/**").permitAll()
                        // Endpoint cho ROLE_ADMINISTRATOR hoặc ADMIN
                        .requestMatchers("/admin/query/all").permitAll()
                       // .requestMatchers("/admin/degree/info/username/**").permitAll()
                        .requestMatchers("/admin/degree/info/username/**",
                                "/admin/degree/img/username").hasAuthority("ROLE_STUDENT")
                        .requestMatchers("/admin/user/change").hasAnyAuthority("ROLE_STUDENT", "ROLE_ADMINISTRATOR", "ROLE_ADMIN")
                        .requestMatchers("/admin/degree/by-year",
                                "/admin/degree/getall",
                                "/admin/degree/add",
                                "/admin/degree/update",
                                "/admin/degree/*",
                                "/admin/degree/student/*",
                                "/admin/degree/info/*",
                                "/admin/degree/img/*",
                                "/admin/degree/by-classification",
                                "/admin/degree/academic-years",
                                 "/admin/degree/list",
                                "/admin/degree/export-excel",
                                "/admin/permission/all",
                                "/admin/role/all",
                                "/admin/rolepermission/all",
                                "/admin/user/all",
                                "/admin/user/search/role/*",
                                "/admin/user/search/name/*",
                                "/admin/user/add",
                                "/admin/user/update",
                                "/admin/faculty/**",
                                "/admin/major/**",
                                "/admin/student/**",
                                "/admin/student/review/**",
                                "/admin/student/search/**",
                                "/admin/student/delete/*",
                                "/admin/user/delete/*",
                                "/admin/user/search/**",
                                "/admin/user/unlock/*").hasAnyAuthority("ROLE_ADMINISTRATOR", "ROLE_ADMIN")
                        // Endpoint chỉ cho ROLE_ADMINISTRATOR
                        .requestMatchers("/admin/user/**",
                                "/admin/student/**",
                                "/admin/rolepermission/**",
                                "/admin/role/**",
                                "/admin/permission",
                                "/degree/export/**",
                                "/admin/query/**",
                                "/admin/degree/**").hasAuthority("ROLE_ADMINISTRATOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
