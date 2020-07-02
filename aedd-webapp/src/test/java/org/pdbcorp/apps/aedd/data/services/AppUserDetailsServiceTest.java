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
package org.pdbcorp.apps.aedd.data.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pdbcorp.apps.aedd.data.models.Role;
import org.pdbcorp.apps.aedd.data.models.User;
import org.pdbcorp.apps.aedd.data.repos.RoleRepository;
import org.pdbcorp.apps.aedd.data.repos.UserRepository;
import org.pdbcorp.apps.aedd.services.AppUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author jaradat-pdb
 *
 */
@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	@Mock
	private UserRepository userRepository;
	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private AppUserDetailsService appUserDetailsService = new AppUserDetailsService(bCryptPasswordEncoder, roleRepository, userRepository);

	@DisplayName("Give user object When save Then return true")
	@Test
	void validSaveUser() throws Exception {
		Role role = new Role("USER");
		User user = new User("John", "Doe", "john.doe@email.com", "password");
		user.setId("test_validSave");
		when(roleRepository.findByName((String) anyString())).thenReturn(role);
		when(userRepository.save(any(User.class))).thenReturn(user);
		assertTrue(appUserDetailsService.saveUser(user));
	}

	@DisplayName("Give email string When lookup Then return user")
	@Test
	void validFindUserByEmail() throws Exception {
		String email = "john.doe@email.com";
		User user = new User("John", "Doe", email, "password");
		when(userRepository.findByEmail(email)).thenReturn(user);
		assertEquals(user, appUserDetailsService.findUserByEmail(email));
	}

	@DisplayName("Give email string When lookup Then return userDetails")
	@Test
	void validLoadUserByUsername() throws Exception {
		Role role = new Role("USER");
		String email = "john.doe@email.com";
		User user = new User("John", "Doe", email, "password", true, Arrays.asList(role));
		when(userRepository.findByEmail(anyString())).thenReturn(user);
		assertTrue(appUserDetailsService.loadUserByUsername(email) instanceof UserDetails);
	}

	@DisplayName("Give non-existing email string When lookup Then throw usernameNotFoundException")
	@Test
	void errorLoadUserByUsername() throws Exception {
		String expectedMsg = "Provided email does not exist in the system";
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		Exception e = assertThrows(UsernameNotFoundException.class, () -> {
			appUserDetailsService.loadUserByUsername("abc@123.com");
		});
		assertEquals(expectedMsg, e.getMessage());
	}
}
