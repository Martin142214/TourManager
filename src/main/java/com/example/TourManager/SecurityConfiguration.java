package com.example.TourManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import com.example.TourManager.Services.AuthServices.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration {
 
    @Autowired
    private UserDetailsServiceImpl myUserDetailsService;
     

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;

    }

    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExprHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    /*@Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImplementation();
    }*/
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeHttpRequests()
            .anyRequest().permitAll()
            .and()
			.formLogin()
				.loginPage("/user/login")
                .defaultSuccessUrl("/", true)
				.permitAll()
                .and()
			.logout((logout) -> logout.permitAll()
                                      .logoutSuccessUrl("/"));

		return http.build();
    }


    /* 
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/users").authenticated()
            .anyRequest().permitAll()
            .and()
            .formLogin()
                .usernameParameter("username")
                .loginProcessingUrl("/register")
                .defaultSuccessUrl("/users")
                .permitAll()
            .and()
             .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("users")
            .and()
            .logout().logoutSuccessUrl("/").permitAll();
    }*/
    
 



    //@Bean
	//public UserDetailsService userDetailsService() {
		/*UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username(this.username)
				.password(this.password)
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);*/
	//}
}


