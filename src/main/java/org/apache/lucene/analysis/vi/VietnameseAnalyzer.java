package org.apache.lucene.analysis.vi;

import org.apache.lucene.analysis.*;
import vn.pipeline.VnCoreNLP;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class VietnameseAnalyzer extends StopwordAnalyzerBase {
    public static final CharArraySet VIETNAMESE_STOP_WORDS_SET;
    private final VnCoreNLP vnCoreNLP;

    static {
        final List<String> stopWords = Arrays.asList(
                "bị", "bởi", "cả", "các", "cái", "cần", "càng", "chỉ", "chiếc", "cho", "chứ", "chưa", "chuyện",
                "có", "có thể", "cứ", "của", "cùng", "cũng", "đã", "đang", "đây", "để", "đến nỗi", "đều", "điều",
                "do", "đó", "được", "dưới", "gì", "khi", "không", "là", "lại", "lên", "lúc", "mà", "mỗi", "một cách",
                "này", "nên", "nếu", "ngay", "nhiều", "như", "nhưng", "những", "nơi", "nữa", "phải", "qua", "ra",
                "rằng", "rằng", "rất", "rất", "rồi", "sau", "sẽ", "so", "sự", "tại", "theo", "thì", "trên", "trước",
                "từ", "từng", "và", "vẫn", "vào", "vậy", "vì", "việc", "với", "vừa"
        );
        final CharArraySet stopSet = new CharArraySet(stopWords, false);
        VIETNAMESE_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
    }


    /**
     * Returns an unmodifiable instance of the default stop words set.
     *
     * @return default stop words set.
     */
    public static CharArraySet getDefaultStopSet() {
        return DefaultSetHolder.DEFAULT_STOP_SET;
    }

    /**
     * Atomically loads the DEFAULT_STOP_SET in a lazy fashion once the outer class
     * accesses the static final set the first time.
     */
    private static class DefaultSetHolder {
        static final CharArraySet DEFAULT_STOP_SET = VIETNAMESE_STOP_WORDS_SET;
    }

    /**
     * Builds an analyzer with the default stop words: {@link #getDefaultStopSet}.
     */
    public VietnameseAnalyzer() {
        this(DefaultSetHolder.DEFAULT_STOP_SET);
    }

    /**
     * Builds an analyzer with the default stop words
     */
    public VietnameseAnalyzer(CharArraySet stopWords) {
        super(stopWords);
        try {
            this.vnCoreNLP = new VnCoreNLP(new String[]{"wseg"});
        } catch (IOException e) {
            throw new IllegalStateException("Cannot instantiate vnCoreNLP", e);
        }
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer tokenizer = new VietnameseTokenizer(this.vnCoreNLP);
        TokenStream tokenStream = new LowerCaseFilter(tokenizer);
        tokenStream = new StopFilter(tokenStream, stopwords);
        return new TokenStreamComponents(tokenizer, tokenStream);
    }
}
