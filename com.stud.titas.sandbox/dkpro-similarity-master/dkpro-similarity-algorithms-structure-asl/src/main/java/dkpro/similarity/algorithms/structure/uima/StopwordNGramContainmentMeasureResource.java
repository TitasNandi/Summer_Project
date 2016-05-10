/*******************************************************************************
 * Copyright 2012
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
package dkpro.similarity.algorithms.structure.uima;

import java.io.IOException;
import java.util.Map;

import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;

import dkpro.similarity.algorithms.structure.StopwordNGramContainmentMeasure;
import dkpro.similarity.uima.resource.TextSimilarityResourceBase;


public class StopwordNGramContainmentMeasureResource
	extends TextSimilarityResourceBase
{
	public static final String PARAM_STOPWORD_LIST_LOCATION = "StopwordListLocation";
	@ConfigurationParameter(name=PARAM_STOPWORD_LIST_LOCATION, mandatory=true)
	private String stopwordListLocation;
	
    public static final String PARAM_N = "N";
    @ConfigurationParameter(name=PARAM_N, mandatory=true)
    private String nString;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean initialize(ResourceSpecifier aSpecifier, Map aAdditionalParams)
        throws ResourceInitializationException
    {
        if (!super.initialize(aSpecifier, aAdditionalParams)) {
            return false;
        }

        this.mode = TextSimilarityResourceMode.list;
        
        try {
            int n = Integer.parseInt(nString); 
            measure = new StopwordNGramContainmentMeasure(n, stopwordListLocation);
        }
        catch (IOException e) {
            throw new ResourceInitializationException(e);
        }
        catch (NumberFormatException e) {
            throw new ResourceInitializationException(e);
        }
        
        return true;
    }
}
