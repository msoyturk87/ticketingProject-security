package com.cybertek.config;

import com.cybertek.service.SecurityService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // this annotation for sec
public class SecurityConfig extends WebSecurityConfigurerAdapter {  // to use sec extend it

    private SecurityService securityService;
    private AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                    .antMatchers("/user/**").hasAuthority("Admin")
                    // this part limit the page but we can see related pages in html.
                    // To hide this pages in Html we should add some isAuth in HTML part
                    .antMatchers("/project/**").hasAuthority("Manager")
                    .antMatchers("/task/employee/**").hasAuthority("Employee")
                    .antMatchers("/task/**").hasAuthority("Manager")
                    .antMatchers(
                        "/",
                        "/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**"
                ).permitAll()   // it means we don't need auth for this page even guest user can see
                .and()
                .formLogin()
                    .loginPage("/login")
                // .defaultSuccessUrl("/welcome")
                    .successHandler(authSuccessHandler)
                    .failureUrl("/login?error=true")
                    .permitAll()
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                    .tokenValiditySeconds(120)
                    .key("cybertekSecret")
                    .userDetailsService(securityService);
        }
}
