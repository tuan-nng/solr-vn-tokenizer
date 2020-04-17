package org.apache.lucene.analysis.vi;


import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import vn.pipeline.VnCoreNLP;

import java.io.IOException;
import java.util.Map;

public class VietnameseTokenizerFactory extends TokenizerFactory {
    private final VnCoreNLP vnCoreNLP;

    public VietnameseTokenizerFactory(Map<String, String> args) {
        super(args);
        try {
            vnCoreNLP = new VnCoreNLP(new String[]{"wseg"});
        } catch (IOException e) {
            throw new IllegalStateException("Cannot instantiate vnCoreNLP", e);
        }
    }

    @Override
    public Tokenizer create(AttributeFactory attributeFactory) {
        return new VietnameseTokenizer(vnCoreNLP);
    }
}
