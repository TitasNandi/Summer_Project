package org.grobid.trainer;

import org.grobid.core.GrobidModels;
import org.grobid.core.exceptions.GrobidException;
import org.grobid.core.mock.MockContext;
import org.grobid.core.utilities.GrobidProperties;
import org.grobid.trainer.sax.TEIFulltextSaxParser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * @author Patrice Lopez
 */
public class FulltextTrainer extends AbstractTrainer{

    public FulltextTrainer() {
        super(GrobidModels.FULLTEXT);
    }


    @Override
    public int createCRFPPData(File corpusPath, File outputFile) {
        return addFeaturesFulltext(corpusPath.getAbsolutePath() + "/tei", corpusPath + "/fulltexts", outputFile);
    }

	/**
	 * Add the selected features to a full text example set 
	 * 
	 * @param corpusDir
	 *            a path where corpus files are located
	 * @param trainingOutputPath
	 *            path where to store the temporary training data
	 * @param evalOutputPath
	 *            path where to store the temporary evaluation data
	 * @param splitRatio
	 *            ratio to consider for separating training and evaluation data, e.g. 0.8 for 80% 
	 * @return the total number of used corpus items 
	 */
	@Override
	public int createCRFPPData(final File corpusDir, 
							final File trainingOutputPath, 
							final File evalOutputPath, 
							double splitRatio) {
		return 0;						
	}

    /**
     * Add the selected features to the author model training for full texts
     * @param sourceTEIPathLabel path to TEI files
     * @param sourceFulltextsPathLabel path to fulltexts
     * @param outputPath output train file
     * @return number of examples
     */
    public int addFeaturesFulltext(String sourceTEIPathLabel,
                                   String sourceFulltextsPathLabel,
                                   File outputPath) {
        int totalExamples = 0;
        try {
            System.out.println("sourceTEIPathLabel: " + sourceTEIPathLabel);
            System.out.println("sourceFulltextsPathLabel: " + sourceFulltextsPathLabel);
            System.out.println("outputPath: " + outputPath);

            // we need first to generate the labeled files from the TEI annotated files
            File input = new File(sourceTEIPathLabel);
            // we process all tei files in the output directory
            File[] refFiles = input.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".tei.xml");
                }
            });

            if (refFiles == null) {
                return 0;
            }

            System.out.println(refFiles.length + " tei files");

            // the file for writing the training data
            OutputStream os2 = new FileOutputStream(outputPath);
            Writer writer2 = new OutputStreamWriter(os2, "UTF8");

            // get a factory for SAX parser
            SAXParserFactory spf = SAXParserFactory.newInstance();

//            int n = 0;
            for (File tf : refFiles) {
                String name = tf.getName();
                System.out.println(name);

                TEIFulltextSaxParser parser2 = new TEIFulltextSaxParser();
				//parser2.setMode(TEIFulltextSaxParser.FULLTEXT);
				
                //get a new instance of parser
                SAXParser p = spf.newSAXParser();
                p.parse(tf, parser2);

                List<String> labeled = parser2.getLabeledResult();
                //totalExamples += parser2.n;

                // we can now add the features
                // we open the featured file
                int q = 0;
                BufferedReader bis = new BufferedReader(
                        new InputStreamReader(new FileInputStream(
                                sourceFulltextsPathLabel + File.separator + 
								name.replace(".tei.xml", "")), "UTF8"));

                StringBuilder fulltext = new StringBuilder();

                String line;
//                String lastTag = null;
                while ((line = bis.readLine()) != null) {
                    //fulltext.append(line);
                    int ii = line.indexOf(' ');
                    String token = null;
                    if (ii != -1)
                        token = line.substring(0, ii);
//                    boolean found = false;
                    // we get the label in the labelled data file for the same token
                    for (int pp = q; pp < labeled.size(); pp++) {
                        String localLine = labeled.get(pp);
                        StringTokenizer st = new StringTokenizer(localLine, " ");
                        if (st.hasMoreTokens()) {
                            String localToken = st.nextToken();

                            if (localToken.equals(token)) {
                                String tag = st.nextToken();
                                fulltext.append(line).append(" ").append(tag);
//                                lastTag = tag;
//                                found = true;
                                q = pp + 1;
                                pp = q + 10;
                            }
                        }
                        if (pp - q > 5) {
                            break;
                        }
                    }
                    /*if (!found) {
                             if (lastTag != null)
                                 header.append(lastTag);
                         }*/
                    //fulltext.append("\n");
                }
                bis.close();

                //String fulltext = FeatureTrainerUtil.addFeaturesFulltext(labeled, false);
                //doc.getFulltextFeatured(boolean firstPass, boolean getHeader);

                // format with features for sequence tagging...
                writer2.write(fulltext.toString() + "\n");
            }

            writer2.close();
            os2.close();
        } catch (Exception e) {
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
        return totalExamples;
    }

    /**
     * Command line execution.
     *
     * @param args Command line arguments.
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
    	MockContext.setInitialContext();
    	GrobidProperties.getInstance();
        AbstractTrainer.runTraining(new FulltextTrainer());
        AbstractTrainer.runEvaluation(new FulltextTrainer());
        MockContext.destroyInitialContext();
    }
}	