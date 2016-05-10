package org.grobid.core.tokenization;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import org.grobid.core.GrobidModels;
import org.grobid.core.engines.TaggingLabel;
import org.grobid.core.layout.LayoutToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zholudev on 12/01/16.
 * Cluster tokens by label
 */

public class TaggingTokenClusteror {
    private final TaggingTokenSynchronizer taggingTokenSynchronizer;

    public static class LabelTypePredicate implements Predicate<TaggingTokenCluster> {
        private TaggingLabel label;

        public LabelTypePredicate(TaggingLabel label) {
            this.label = label;
        }

        @Override
        public boolean apply(TaggingTokenCluster taggingTokenCluster) {
            return taggingTokenCluster.getTaggingLabel() == label;
        }
    }


    public TaggingTokenClusteror(GrobidModels grobidModel, String result, List<LayoutToken> tokenizations) {
        taggingTokenSynchronizer = new TaggingTokenSynchronizer(grobidModel, result, tokenizations);
    }

    public TaggingTokenClusteror(GrobidModels grobidModel, String result, List<LayoutToken> tokenizations,
                                 boolean computerFeatureBlock) {
        taggingTokenSynchronizer = new TaggingTokenSynchronizer(grobidModel, result, tokenizations, computerFeatureBlock);
    }

    public List<TaggingTokenCluster> cluster() {
        List<TaggingTokenCluster> result = new ArrayList<>();

        PeekingIterator<LabeledTokensContainer> it = Iterators.peekingIterator(taggingTokenSynchronizer);
        if (!it.hasNext()) {
            return Collections.emptyList();
        }

        TaggingTokenCluster curCluster = new TaggingTokenCluster(it.peek().getTaggingLabel());
        while (it.hasNext()) {
            LabeledTokensContainer cont = it.next();
            if (cont.isBeginning() || cont.getTaggingLabel() != curCluster.getTaggingLabel()) {
                curCluster = new TaggingTokenCluster(cont.getTaggingLabel());
                result.add(curCluster);
            }
            curCluster.addLabeledTokensContainer(cont);
        }

        return result;
    }

}
