package org.apache.lucene.analysis.vi;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import vn.hus.nlp.tokenizer.tokens.TaggedWord;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Vietnamese Tokenizer.
 *
 * @author duydo
 */
public class VietnameseTokenizer extends Tokenizer {
    private List<TaggedWord> pending = new CopyOnWriteArrayList<>();

    private int offset = 0;
    private int pos = 0;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);

    private final me.duydo.vi.Tokenizer tokenizer;
    private String inputText;
    private boolean isInitialized = false;

    public VietnameseTokenizer(me.duydo.vi.Tokenizer tokenizer) {
        super();
        this.tokenizer = tokenizer;
    }

    private void tokenize() throws IOException {
        inputText = IOUtils.toString(input);
        final List<TaggedWord> result = tokenizer.tokenize(new StringReader(inputText));
        if (result != null) {
            pending.addAll(result);
        }
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (!isInitialized) {
            tokenize();
            isInitialized = true;
        }
        if (pending.size() == 0) {
            return false;
        }
        clearAttributes();

        for (int i = pos; i < pending.size(); i++) {
            pos++;
            final TaggedWord word = pending.get(i);
            if (accept(word)) {
                posIncrAtt.setPositionIncrement(1);
                final int length = word.getText().length();
                typeAtt.setType(String.format("<%s>", word.getRule().getName().toUpperCase()));
                termAtt.copyBuffer(word.getText().toCharArray(), 0, length);
                final int start = inputText.indexOf(word.getText(), offset);
                offsetAtt.setOffset(correctOffset(start), offset = correctOffset(start + length));
                return true;
            }
        }
        return false;
    }

    /**
     * Only accept the word characters.
     */
    private final boolean accept(TaggedWord word) {
        final String type = word.getRule().getName().toLowerCase();
        if ("punctuation".equals(type) || "special".equals(type)) {
            return false;
        }
        return true;
    }

    @Override
    public final void end() throws IOException {
        super.end();
        final int finalOffset = correctOffset(offset);
        offsetAtt.setOffset(finalOffset, finalOffset);
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        pos = 0;
        offset = 0;
        pending.clear();
        isInitialized = false;
    }
}
