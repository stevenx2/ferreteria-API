package org.ferreteria.security;


import org.ferreteria.problem.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuraciones de seguridad de la api con autenticación
 */
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityCfg {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /**
                 * Solo administradores puede eliminar o actualizar cualquier recurso (por ahora)
                 */
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(
                            AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/**")
                    ).hasRole("ADMIN");

                    authorize.requestMatchers(
                            AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/**")
                    ).hasRole("ADMIN");

                    authorize.anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                // este deniedHandler que registré permite devolver una respuesta en formato json cuando alguien quiera acceder a un recurso al cual no está autorizado
                .exceptionHandling(exception -> exception.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());


        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        User.UserBuilder users = User.builder().passwordEncoder(encoder::encode);

        UserDetails admin = users
                .password("12345")
                .username("admin")
                .roles("ADMIN")
                .build();

        UserDetails user = users
                .password("12345")
                .username("user")
                .roles("USER")
                .build();

        UserDetails userAdmin = users
                .password("12345")
                .username("user2")
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin, userAdmin, user);
    }
}
