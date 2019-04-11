# Solr VN Tokenizer

A fork from https://github.com/duydo/elasticsearch-analysis-vietnamese to support Solr

The plugin supports `VietnameseAnalyzer` and `VietnameseTokenizer`.

* `VietnameseAnalyzer` combines `VietnameseTokenizer`, `LowerCaseFilter` and `StopFilter`.

#### Install

1. Build nlp dependencies
```text
git clone https://github.com/duydo/vn-nlp-libraries.git
cd vn-nlp-libraries/nlp-parent
mvn install
```

2. Build the plugin
```text
git clone https://github.com/tuan-nng/solr-vn-tokenizer
cd solr-vn-tokenizer
mvn package
```
3. Install plugin
 
Copy jar file in `target` folder into solr server directory: `<solr dir>/server/lib`  
And it will be available to update your type in Solr UI.  

An example `fieldType` is as below.
```text
<fieldType name="text_vn" class="solr.TextField">
    <analyzer type="index" class="org.apache.lucene.analysis.vi.VietnameseAnalyzer"/>
    <analyzer type="query" class="org.apache.lucene.analysis.vi.VietnameseAnalyzer"/>
</fieldType>
```

#### Thanks to
* [Duy Do](https://github.com/duydo/elasticsearch-analysis-vietnamese) for his work for Vietnamese Analysis Plugin for Elasticsearch
* [Lê Hồng Phương](http://mim.hus.vnu.edu.vn/phuonglh/) for his VnTokenizer library

