package org.apache.lucene.analysis.vi;


import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

public class VietnameseTokenizerFactory extends TokenizerFactory {
    private final me.duydo.vi.Tokenizer tokenizer;

    public VietnameseTokenizerFactory(Map<String, String> args) {
        super(args);
        tokenizer = AccessController.doPrivileged((PrivilegedAction<me.duydo.vi.Tokenizer>) me.duydo.vi.Tokenizer::new);
    }

    @Override
    public Tokenizer create(AttributeFactory attributeFactory) {
        return new VietnameseTokenizer(tokenizer);
    }
}
