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

/* First created by JCasGen Wed May 23 16:07:44 CEST 2012 */
package dkpro.similarity.uima.api.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Sat Sep 28 15:25:36 CEST 2013
 * @generated */
public class TextSimilarityScore_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TextSimilarityScore_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TextSimilarityScore_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TextSimilarityScore(addr, TextSimilarityScore_Type.this);
  			   TextSimilarityScore_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TextSimilarityScore(addr, TextSimilarityScore_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TextSimilarityScore.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("dkpro.similarity.uima.api.type.TextSimilarityScore");
 
  /** @generated */
  final Feature casFeat_Score;
  /** @generated */
  final int     casFeatCode_Score;
  /** @generated */ 
  public double getScore(int addr) {
        if (featOkTst && casFeat_Score == null)
      jcas.throwFeatMissing("Score", "dkpro.similarity.uima.api.type.TextSimilarityScore");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_Score);
  }
  /** @generated */    
  public void setScore(int addr, double v) {
        if (featOkTst && casFeat_Score == null)
      jcas.throwFeatMissing("Score", "dkpro.similarity.uima.api.type.TextSimilarityScore");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_Score, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TextSimilarityScore_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Score = jcas.getRequiredFeatureDE(casType, "Score", "uima.cas.Double", featOkTst);
    casFeatCode_Score  = (null == casFeat_Score) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Score).getCode();

  }
}



    