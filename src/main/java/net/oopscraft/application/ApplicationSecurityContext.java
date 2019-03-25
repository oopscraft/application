package net.oopscraft.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import net.oopscraft.application.user.security.AuthenticationProvider;

//@EnableWebSecurity
public class ApplicationSecurityContext extends WebSecurityConfigurerAdapter {
 
    @Autowired
    private AuthenticationProvider authenticationProvider;
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.authorizeRequests()
//                .antMatchers("/css/**", "/js/**", "/img/**").permitAll()
//                .antMatchers("/auth/admin/**").hasRole("ADMIN")
//                .antMatchers("/auth/**").hasAnyRole("ADMIN", "USER")
//                .anyRequest().authenticated();
// 
//        http.formLogin()
//                .loginPage("/login") // default
//                .loginProcessingUrl("/authenticate")
//                .failureUrl("/login?error") // default
//                .defaultSuccessUrl("/home")
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .permitAll();
// 
//        http.logout()
//                .logoutUrl("/logout") // default
//                .logoutSuccessUrl("/login")
//                .permitAll();
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
}