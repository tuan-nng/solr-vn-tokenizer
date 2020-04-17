package org.apache.lucene.analysis.vi;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.junit.jupiter.api.Test;
import vn.pipeline.VnCoreNLP;

import java.io.IOException;
import java.io.StringReader;

public class VietnameseTokenizerTest extends BaseTokenStreamTestCase {

    @Test
    void tokenizeVietnamese() throws IOException {
        VietnameseTokenizer tokenizer = new VietnameseTokenizer(new VnCoreNLP(new String[]{"wseg"}));
        tokenizer.setReader(new StringReader("Công nghệ thông tin Việt Nam"));
        assertTokenStreamContents(tokenizer, new String[]{"Công nghệ thông tin", "Việt Nam"});

        tokenizer.setReader(new StringReader("Hà Nội mùa này vắng những cơn mưa."));
        assertTokenStreamContents(tokenizer, new String[]{"Hà Nội", "mùa", "này", "vắng", "những", "cơn", "mưa", "."});

        tokenizer.setReader(new StringReader("động vật trên thế giới rất phong phú và đa dạng"));
        assertTokenStreamContents(tokenizer, new String[]{"động vật", "trên", "thế giới", "rất", "phong phú", "và", "đa dạng"});

        tokenizer.setReader(new StringReader("\ncho tam giác ABC"));
        assertTokenStreamContents(tokenizer, new String[]{"cho", "tam giác", "ABC"});
    }

    @Test
    void tokenizeSpace() throws IOException {
        VietnameseTokenizer tokenizer = new VietnameseTokenizer(new VnCoreNLP(new String[]{"wseg"}));
        tokenizer.setReader(new StringReader("Hà   Nội mùa  này vắng những cơn mưa Hà_Nội."));
        assertTokenStreamContents(tokenizer, new String[]{"Hà Nội", "mùa", "này", "vắng", "những", "cơn", "mưa", "Hà_Nội", "."});

        tokenizer.setReader(new StringReader("Hà   \nNội mùa  này vắng những cơn mưa Hà_Nội."));
        assertTokenStreamContents(tokenizer, new String[]{"Hà Nội", "mùa", "này", "vắng", "những", "cơn", "mưa", "Hà_Nội", "."});

        tokenizer.setReader(new StringReader("hòa tan hoàn toàn Y"));
        assertTokenStreamContents(tokenizer, new String[]{"hòa tan", "hoàn toàn", "Y"});
    }

    @Test
    void tokenizerSpecialText() throws IOException {
        VietnameseTokenizer tokenizer = new VietnameseTokenizer(new VnCoreNLP(new String[]{"wseg"}));
        tokenizer.setReader(new StringReader("làm mát khí nạp- cao su lưu hóa"));
        assertTokenStreamContents(tokenizer, new String[]{"làm", "mát", "khí", "nạp", "-", "cao su", "lưu hóa"});

        tokenizer.setReader(new StringReader("Nguyễn Duy?"));
        assertTokenStreamContents(tokenizer, new String[]{"Nguyễn Duy?"});

        tokenizer.setReader(new StringReader("A:(2)"));
        assertTokenStreamContents(tokenizer, new String[]{"A:", "(", "2", ")"});

        tokenizer.setReader(new StringReader("biểu diễn chúng trên trục số a) (-12"));
        assertTokenStreamContents(tokenizer, new String[]{"biểu diễn", "chúng", "trên", "trục", "số", "a)", "(", "-12"});
    }
}
