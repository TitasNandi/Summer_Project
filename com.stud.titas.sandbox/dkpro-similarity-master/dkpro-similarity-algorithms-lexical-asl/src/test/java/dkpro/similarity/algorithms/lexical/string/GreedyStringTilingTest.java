/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package dkpro.similarity.algorithms.lexical.string;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dkpro.similarity.algorithms.api.TermSimilarityMeasure;
import dkpro.similarity.algorithms.lexical.string.GreedyStringTiling;
   

public class GreedyStringTilingTest
{
	private static final double epsilon = 0.001;
	
    @Test
    public void test()
		throws Exception
	{
		String a1 = "The quick brown fox jumps over the lazy dog.";
		String a2 = "Lorem ipsum quick brown dolor, consectetur over the nomen tuum.";

		TermSimilarityMeasure measure = new GreedyStringTiling(3);

		assertEquals(0.522, measure.getSimilarity(a1, a2), epsilon);
   }
}
