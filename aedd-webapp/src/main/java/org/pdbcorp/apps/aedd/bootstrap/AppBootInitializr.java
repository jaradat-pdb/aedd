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
package org.pdbcorp.apps.aedd.bootstrap;

import java.util.Arrays;

import org.pdbcorp.apps.aedd.data.models.Role;
import org.pdbcorp.apps.aedd.data.models.User;
import org.pdbcorp.apps.aedd.data.repos.RoleRepository;
import org.pdbcorp.apps.aedd.services.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jaradat-pdb
 *
 */
@Slf4j
@Component
public class AppBootInitializr implements CommandLineRunner {
	private static final String ROLE_LOG_MSG = "Added role: {}";
	private Role roleAdmin = new Role("ADMIN");
	private Role roleRoot = new Role("ROOT");
	private Role roleUser = new Role("USER");
	private User rootUser = new User("root", "admin", "admin@pdbcorp.org", "superSecretPassword", true, Arrays.asList(roleAdmin, roleRoot, roleUser));
	private AppUserDetailsService appUserDetailsService;
	private RoleRepository roleRepository;

	@Autowired
	public AppBootInitializr(AppUserDetailsService appUserDetailsService, RoleRepository roleRepository) {
		this.appUserDetailsService = appUserDetailsService;
		this.roleRepository = roleRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		for(Role role : Arrays.asList(roleAdmin, roleRoot, roleUser)) {
			if(roleRepository.findByName(role.getName()) == null) {
				role = roleRepository.save(role);
				if(log.isTraceEnabled()) {
					log.trace(ROLE_LOG_MSG, role);
				}
			}
		}
		
		if(appUserDetailsService.findUserByEmail(rootUser.getEmail()) == null) {
			log.info("Saved root admin user: {}", appUserDetailsService.saveUser(rootUser));
		}
	}
}
