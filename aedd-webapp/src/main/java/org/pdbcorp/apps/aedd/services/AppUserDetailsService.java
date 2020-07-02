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
package org.pdbcorp.apps.aedd.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pdbcorp.apps.aedd.data.models.Role;
import org.pdbcorp.apps.aedd.data.models.User;
import org.pdbcorp.apps.aedd.data.repos.RoleRepository;
import org.pdbcorp.apps.aedd.data.repos.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jaradat-pdb
 *
 */
@Slf4j
@Service
public class AppUserDetailsService implements UserDetailsService {
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private UserRepository userRepository;
	private RoleRepository roleRepository;

	public AppUserDetailsService(
			BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
		
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public boolean saveUser(User user) {
		boolean opRes;
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		if(user.getRoles() == null || user.getRoles().isEmpty()) {
			user.setRoles(Arrays.asList(roleRepository.findByName("USER")));
		}
		user = userRepository.save(user);
		if(StringUtils.isBlank(user.getId())) {
			opRes = false;
			log.error("Attempted save operation of user [{}] returned a null id parameter", user.getEmail());
		}
		else {
			opRes = true;
			if(log.isTraceEnabled()) {
				log.trace("Successfully saved user: {}", user);
			} else if(log.isDebugEnabled()) {
				log.debug("Successfully saved user [{}] with id: {}", user.getEmail(), user.getId());
			}
		}
		return opRes;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = findUserByEmail(email);
		if(user != null) {
			return buildUserForAuthentication(user, getUserAuthorities(user.getRoles()));
		}
		else {
			throw new UsernameNotFoundException("Provided email does not exist in the system");
		}
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}

	private List<GrantedAuthority> getUserAuthorities(List<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
		return authorities;
	}
}
