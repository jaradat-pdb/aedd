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
package org.pdbcorp.apps.aedd.data.repos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pdbcorp.apps.aedd.data.models.ArWord;
import org.pdbcorp.apps.aedd.utils.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author jaradat-pdb
 *
 */
@ActiveProfiles("test")
@DirtiesContext
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ArWordRepositoryIT {
	@Autowired
	private ArWordRepository arWordRepository;
	@Autowired
	private EnWordRepository enWordRepository;

	@DisplayName("Give arWord object When save Then create new arWord")
	@Test
	void validSaveTest() throws Exception {
		ArWord arWord = TestDataFactory.getArWordInstance(enWordRepository);
		arWordRepository.save(arWord);
		assertThat(arWordRepository.findAll().size()).isGreaterThan(0);
		assertThat(arWordRepository.findByWord(arWord.getWord()).getId()).isNotBlank();
	}
}
