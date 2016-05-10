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
package dkpro.similarity.algorithms.style.uima;

import java.io.IOException;
import java.util.Map;

import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;

import dkpro.similarity.algorithms.style.FunctionWordFrequenciesMeasure;
import dkpro.similarity.uima.resource.JCasTextSimilarityResourceBase;


public class FunctionWordFrequenciesMeasureResource
	extends JCasTextSimilarityResourceBase
{
	public static final String PARAM_FUNCTION_WORD_LIST_LOCATION = "FunctionWordListLocation";
	@ConfigurationParameter(name=PARAM_FUNCTION_WORD_LIST_LOCATION, mandatory=false)
	private String functionWordListLocation;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean initialize(ResourceSpecifier aSpecifier, Map aAdditionalParams)
        throws ResourceInitializationException
    {
        if (!super.initialize(aSpecifier, aAdditionalParams)) {
            return false;
        }

        this.mode = TextSimilarityResourceMode.jcas;
        
        try {
            if (functionWordListLocation != null) {
                measure = new FunctionWordFrequenciesMeasure(functionWordListLocation);
            }
            else {
                measure = new FunctionWordFrequenciesMeasure();
            }
        }
        catch (IOException e) {
            throw new ResourceInitializationException(e);
        }
        
        return true;
    }
}
