package org.apache.lucene.analysis.vi;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class VietnameseAnalyzerTest extends BaseTokenStreamTestCase {
    @Test
    void analyzeVietnamese() throws IOException {
        VietnameseAnalyzer analyzer = new VietnameseAnalyzer();
        TokenStream tokenStream = analyzer.tokenStream("test", "Hà Nội mùa này vắng những cơn mưa");
        assertTokenStreamContents(tokenStream, new String[]{"hà", "nội", "mùa", "vắng", "cơn", "mưa"});
    }
}
