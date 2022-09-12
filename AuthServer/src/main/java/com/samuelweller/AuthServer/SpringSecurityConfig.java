package com.samuelweller.AuthServer;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@EnableWebSecurity
public class SpringSecurityConfig {

	@Bean
	SecurityFilterChain configureSecurityFilterChain(HttpSecurity http) throws Exception {

		http
		
		//Allow options requests
		
		.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
		
		// All other requests must be authenticated using form login
		.anyRequest().authenticated().and().formLogin(Customizer.withDefaults());

		return http.build();
	}
	
	 @Bean
		@Order(Ordered.HIGHEST_PRECEDENCE)
	    CorsConfigurationSource corsConfigurationSource() {
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        CorsConfiguration corsConfiguration = new CorsConfiguration();
	        corsConfiguration.addAllowedOrigin("http://127.0.0.1:3000");
	        corsConfiguration.setAllowedMethods(Arrays.asList(
	                HttpMethod.GET.name(),
	                HttpMethod.HEAD.name(),
	                HttpMethod.POST.name(),
	                HttpMethod.PUT.name(),
	                HttpMethod.DELETE.name()));
	        corsConfiguration.setMaxAge(1800L);
	        source.registerCorsConfiguration("/**", corsConfiguration); // you restrict your path here
	        return source;
	    }
	
	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
		return context -> {

			if (context.getTokenType() == OAuth2TokenType.ACCESS_TOKEN) {
				Authentication principal = context.getPrincipal();
				Set<String> authorities = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toSet());
				context.getClaims().claim("role", authorities);
			}

		};
	}
	
	

	@Bean
	public UserDetailsService users() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		// Add ourselves as a user
		UserDetails user = User.withUsername("sweller").password(encoder.encode("sweller")).authorities("TOP SECRET").build();

		user.getAuthorities().forEach(System.out::println);

		UserDetails user2 = User.withUsername("sweller2").password(encoder.encode("sweller2")).roles("OFFICIAL").build();

		user2.getAuthorities().forEach(System.out::println);

		return new InMemoryUserDetailsManager(user, user2);
	}

}
