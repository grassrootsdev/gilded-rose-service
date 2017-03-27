package init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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
                //only allow members of USER group to post to send POST to /items, i.e., buy
                .antMatchers(HttpMethod.POST, "/items/*").hasRole("USER") 
                .anyRequest().authenticated()
                .and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
            .httpBasic() //allows for programatic login
            	.and()
            .rememberMe()
            	.tokenValiditySeconds(SECONDS_IN_HOUR)
            	.and()
            .csrf()      //i'd want to talk with security person at work before using this option in prod
            	.disable()
            .logout()
                .permitAll();
    }

    /**
     * This store would reach out to db or other secure store in prod environment
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}