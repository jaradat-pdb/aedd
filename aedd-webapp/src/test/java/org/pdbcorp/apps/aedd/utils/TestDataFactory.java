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
package org.pdbcorp.apps.aedd.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pdbcorp.apps.aedd.data.models.ArWord;
import org.pdbcorp.apps.aedd.data.models.EnWord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author jaradat-pdb
 *
 */
public final class TestDataFactory {
	public static final ArWord getArWordInstance(MongoRepository<EnWord, String> enWordRepository) {
		char[] root = {'ع', 'و', 'ر'};
		List<EnWord> initialEnWords = Arrays.asList(new EnWord("genital(s)"), new EnWord("private part(s)"), new EnWord("mistake(s)"), new EnWord("error(s)"));
		List<EnWord> enWords = new ArrayList<>();
		for(EnWord enWord : initialEnWords) {
			enWords.add(enWordRepository.save(enWord));
		}
		return new ArWord("عَوْرَة", "noun", root, enWords);
	}
}
