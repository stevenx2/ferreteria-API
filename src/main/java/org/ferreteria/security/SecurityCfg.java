package org.ferreteria.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * configuraciones de seguridad de la api con autenticación
 */
@Configuration
@EnableWebSecurity
public class SecurityCfg {




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> {
                   authorize.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());


        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
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
                .roles("USER","ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin,userAdmin,user);
    }
}
