package org.apache.lucene.analysis.vi;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.Map;

public class VietnameseStopFilterFactory extends TokenFilterFactory {
    /**
     * Initialize this factory via a set of key-value pairs.
     *
     * @param args
     */
    public VietnameseStopFilterFactory(Map<String, String> args) {
        super(args);
    }

    @Override
    public TokenStream create(TokenStream input) {
        return new StopFilter(input, VietnameseAnalyzer.VIETNAMESE_STOP_WORDS_SET);
    }
}
