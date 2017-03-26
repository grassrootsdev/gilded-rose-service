package init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final int SECONDS_IN_HOUR = 60*60;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/home", "/items").permitAll()
                .antMatchers(HttpMethod.POST, "/items").hasRole("USER")
                .anyRequest().authenticated()
                .and()
//            .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
            .httpBasic()
            	.and()
            .rememberMe()
            	.tokenValiditySeconds(SECONDS_IN_HOUR)
            	.and()
            .csrf()
            	.disable()
            .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}