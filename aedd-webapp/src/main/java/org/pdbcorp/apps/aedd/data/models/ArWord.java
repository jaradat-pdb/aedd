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
package org.pdbcorp.apps.aedd.data.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <strong>Arabic Word</strong>
 * 
 * @author jaradat-pdb
 *
 */
@AllArgsConstructor
@Data
@Document(collection="ar_words")
@NoArgsConstructor
public class ArWord {
	@Transient
	public static final String SEQUENCE_NAME = "ar_words_sequence";

	@Id
	private String id;

	private String word;

	private String classification;

	private char[] root;

	@DBRef
	private List<EnWord> englishWords;

	public ArWord(String word, String classification, char[] root, List<EnWord> englishWords) {
		this.word = word;
		this.classification = classification;
		this.root = root;
		this.englishWords = englishWords;
	}
}
