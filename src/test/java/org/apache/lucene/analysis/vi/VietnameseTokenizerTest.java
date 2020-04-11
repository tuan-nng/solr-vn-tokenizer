package org.apache.lucene.analysis.vi;

import me.duydo.vi.Tokenizer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

public class VietnameseTokenizerTest extends BaseTokenStreamTestCase {

    @Test
    void tokenizeVietnamese() throws IOException {
        VietnameseTokenizer tokenizer = new VietnameseTokenizer(new Tokenizer());
        tokenizer.setReader(new StringReader("Công nghệ thông tin Việt Nam"));
        assertTokenStreamContents(tokenizer, new String[]{"Công nghệ thông tin", "Việt", "Nam"});

        tokenizer.setReader(new StringReader("Hà Nội mùa này vắng những cơn mưa."));
        assertTokenStreamContents(tokenizer, new String[]{"Hà", "Nội", "mùa", "này", "vắng", "những", "cơn", "mưa"});

        tokenizer.setReader(new StringReader("động vật trên thế giới rất phong phú và đa dạng"));
        assertTokenStreamContents(tokenizer, new String[]{"động vật", "trên", "thế giới", "rất", "phong phú", "và", "đa dạng"});

        tokenizer.setReader(new StringReader("\ncho tam giác ABC"));
        assertTokenStreamContents(tokenizer, new String[]{"cho", "tam giác", "ABC"});
    }
}
