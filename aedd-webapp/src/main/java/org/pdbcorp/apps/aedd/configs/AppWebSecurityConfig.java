/*
 * Copyright 2020 PDB Corp.
 *
 * Proprietary Software built off of open-source software?
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pdbcorp.apps.aedd.configs;

import org.pdbcorp.apps.aedd.components.AppAuthenticationSuccessHandler;
import org.pdbcorp.apps.aedd.services.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author jaradat-pdb
 *
 */
@Configuration
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {
	private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;
	private AppUserDetailsService appUserDetailsService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private static final String AUTH_ADMIN = "ADMIN";
	private static final String AUTH_ROOT = "ROOT";
	private static final String URL_LOGOUT = "/logout";
	private static final String URL_LOGOUT_SUCCESS = "/goodbye";

	@Autowired
	public AppWebSecurityConfig(AppAuthenticationSuccessHandler appAuthenticationSuccessHandler,
			AppUserDetailsService appUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		
		this.appAuthenticationSuccessHandler = appAuthenticationSuccessHandler;
		this.appUserDetailsService = appUserDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/img/**", "/js/**", "/**/favicon.ico");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService((UserDetailsService) appUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/home", "/about", "/signup", "/secured").permitAll()
				.antMatchers("/dashboard", "/actuator/health").hasAuthority(AUTH_ADMIN)
				.antMatchers("/actuator/**", "/initiateShutdown").access("hasAuthority('" + AUTH_ROOT + "')")
				.anyRequest().authenticated()
				.and()
				.formLogin()
						.successHandler(appAuthenticationSuccessHandler)
						.loginPage("/login")
						.failureUrl("/login?error=true")
						.usernameParameter("email")
						.passwordParameter("password")
				.permitAll()
				.and()
				.logout()
						.deleteCookies("remove")
						.invalidateHttpSession(true)
						.logoutUrl(URL_LOGOUT)
						.logoutSuccessUrl(URL_LOGOUT_SUCCESS)
						.logoutRequestMatcher(new AntPathRequestMatcher(URL_LOGOUT))
				.permitAll();
		http.exceptionHandling().accessDeniedPage("/403");
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
}
