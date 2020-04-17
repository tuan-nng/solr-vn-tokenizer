package org.apache.lucene.analysis.vi;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import vn.pipeline.Annotation;
import vn.pipeline.VnCoreNLP;
import vn.pipeline.Word;

import java.io.IOException;
import java.util.Iterator;

import static vn.corenlp.wordsegmenter.Utils.NORMALIZER;
import static vn.corenlp.wordsegmenter.Utils.NORMALIZER_KEYS;

/**
 * Vietnamese Tokenizer.
 *
 */
public class VietnameseTokenizer extends Tokenizer {
    private int offset = 0;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);

    private final VnCoreNLP vnCoreNLP;
    private String inputText;
    private String preprocessedText;
    private Iterator<Word> iterator = null;

    public VietnameseTokenizer(VnCoreNLP vnCoreNLP) {
        super();
        this.vnCoreNLP = vnCoreNLP;
    }

    private void tokenize() throws IOException {
        inputText = IOUtils.toString(input);
        preprocessedText = inputText.replaceAll("_", " ");
        for (String key : NORMALIZER_KEYS) {
            preprocessedText = preprocessedText.replaceAll(key, NORMALIZER.get(key));
        }
        Annotation annotation = new Annotation(preprocessedText);
        vnCoreNLP.annotate(annotation);
        iterator = annotation.getWords().iterator();
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (iterator == null) {
            tokenize();
        }

        if (!iterator.hasNext()) {
            return false;
        }
        clearAttributes();

        Word word = iterator.next();
        posIncrAtt.setPositionIncrement(1);
        String form = word.getForm();
        typeAtt.setType(TypeAttribute.DEFAULT_TYPE);
        Location location = indexOf(preprocessedText, form, offset);
        if (location == null) {
            throw new IllegalArgumentException("Token `" + form + "` should appear in the text: " + inputText);
        }
        String term = inputText.substring(location.start, location.end).replaceAll("\\s+", " ");
        termAtt.copyBuffer(term.toCharArray(), 0, term.length());
        offsetAtt.setOffset(correctOffset(location.start), offset = correctOffset(location.end));
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
        offset = 0;
        iterator = null;
    }

    private Location indexOf(String inputText, String word, int offset) {
        if (Strings.isNullOrEmpty(word)) {
            return null;
        }

        int startIndex = -1;
        String[] components = word.split("_+");
        int numOfComponents = components.length;
        for (int i = 0; i < numOfComponents; i++) {
            int start = find(inputText, components[i], offset);
            if (start == -1) {
                return null;
            }
            if (i == 0) {
                startIndex = start;
            }
            offset = start + components[i].length();
        }

        return startIndex == -1 ? null : Location.of(startIndex, offset);
    }

    private int find(String inputText, String token, int offset) {
        int start = inputText.indexOf(token, offset);
        if (start == -1) {
            for (String key : VnCoreNLPUtils.DENORMALIZER_KEYS) {
                if (token.contains(key)) {
                    String replacedToken = token.replaceAll(key, VnCoreNLPUtils.DENORMALIZER.get(key));
                    start = inputText.indexOf(replacedToken, offset);
                    if (start >= 0) {
                        break;
                    }
                }
            }
        }
        return start;
    }

    static class Location {
        int start;
        int end;

        Location(int start, int end) {
            this.start = start;
            this.end = end;
        }

        static Location of(int start, int end) {
            return new Location(start, end);
        }
    }
}
