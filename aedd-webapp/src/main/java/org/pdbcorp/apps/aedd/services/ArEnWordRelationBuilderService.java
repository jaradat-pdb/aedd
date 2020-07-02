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

import org.pdbcorp.apps.aedd.data.repos.ArWordRepository;
import org.pdbcorp.apps.aedd.data.repos.EnWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jaradat-pdb
 *
 */
@Slf4j
@Service
public class ArEnWordRelationBuilderService {
	private ArWordRepository arWordRepository;
	private EnWordRepository enWordRepository;

	@Autowired
	public ArEnWordRelationBuilderService(ArWordRepository arWordRepository, EnWordRepository enWordRepository) {
		this.arWordRepository = arWordRepository;
		this.enWordRepository = enWordRepository;
	}
}
