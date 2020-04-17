package org.apache.lucene.analysis.vi;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static vn.corenlp.wordsegmenter.Utils.NORMALIZER;

public class VnCoreNLPUtils {
    public static Map<String, String> DENORMALIZER;
    public static Set<String> DENORMALIZER_KEYS;

    static {
        DENORMALIZER = NORMALIZER.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        DENORMALIZER_KEYS = DENORMALIZER.keySet();
    }
}
