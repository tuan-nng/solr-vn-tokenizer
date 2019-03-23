package org.apache.lucene.analysis.vi;


import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.util.Map;

public class VietnameseTokenizerFactory extends TokenizerFactory {

    private final boolean sentenceDetectorEnabled;
    private final boolean ambiguitiesResolved;

    public VietnameseTokenizerFactory(Map<String, String> args) {
        super(args);
        this.sentenceDetectorEnabled = getBoolean(args, "sentenceDetectorEnabled", VietnameseTokenizer.DEFAULT_SENTENCE_DETECTOR_ENABLED);
        this.ambiguitiesResolved = getBoolean(args, "ambiguitiesResolved", VietnameseTokenizer.DEFAULT_AMBIGUITIES_RESOLVED);
    }

    @Override
    public Tokenizer create(AttributeFactory attributeFactory) {
        return new VietnameseTokenizer(sentenceDetectorEnabled, ambiguitiesResolved);
    }
}
