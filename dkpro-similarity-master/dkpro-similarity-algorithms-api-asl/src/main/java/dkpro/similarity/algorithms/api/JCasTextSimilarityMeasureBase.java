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
package dkpro.similarity.algorithms.api;

import java.util.Collection;

import org.apache.commons.lang.NotImplementedException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.tcas.DocumentAnnotation;

public abstract class JCasTextSimilarityMeasureBase
	extends TextSimilarityMeasureBase
	implements JCasTextSimilarityMeasure
{
	@Override
	public double getSimilarity(Collection<String> stringList1,
			Collection<String> stringList2)
		throws SimilarityException
	{
		throw new SimilarityException(new NotImplementedException("Use getSimilarity(JCas, JCas) for this measure."));
	}

	@Override
	public double getSimilarity(String string1, String string2)
		throws SimilarityException
	{
		throw new SimilarityException(new NotImplementedException("Use getSimilarity(JCas, JCas) for this measure."));
	}
	
	@Override
	public double getSimilarity(JCas jcas1, JCas jcas2)
		throws SimilarityException
	{		
		Annotation a1 = JCasUtil.selectSingle(jcas1, DocumentAnnotation.class);
		Annotation a2 = JCasUtil.selectSingle(jcas2, DocumentAnnotation.class);
		
		return getSimilarity(jcas1, jcas2, a1, a2);
	}
}
