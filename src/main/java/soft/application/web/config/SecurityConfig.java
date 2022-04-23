package soft.application.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import soft.application.web.dto.Role;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AdminConfig adminConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/auth/*").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/teachers/**").hasAuthority(Role.ADMIN.name())
                    .antMatchers("/applications/**").hasAnyAuthority(Role.ADMIN.name(), Role.TEACHER.name())
                .and()
                .formLogin()
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/applications");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(daoAuthenticationProviderAdmin());
    }

    @Bean()
    public DaoAuthenticationProvider daoAuthenticationProviderAdmin() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails userDetails = User
                .withUsername(adminConfig.getUsername())
                .password(passwordEncoder().encode(adminConfig.getPassword()))
                .authorities(Role.ADMIN.name())
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
