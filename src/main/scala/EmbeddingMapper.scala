import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class EmbeddingMapper extends Mapper[LongWritable, Text, Text, Array[Double]] {
  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Array[Double]]#Context): Unit = {
    // Clean and tokenize the input text
    val strValue = value.toString.split("\n")
    val sentences = new ListBuffer[String].asJava
    strValue.foreach(s => {
      sentences.add(s)
    })
    val t = new DefaultTokenizerFactory()
    t.setTokenPreProcessor(new CommonPreprocessor())

    val iter = new CollectionSentenceIterator(sentences)
    val word2Vec = new Word2Vec.Builder()
      .minWordFrequency(5)
      .iterate(iter)
      .layerSize(100)
      .windowSize(5)
      .tokenizerFactory(t)
      .seed(42)
      .build()
    word2Vec.fit()
    sentences.forEach(sentence => {
      sentence.split("\\s+").foreach(word => {
        context.write(new Text(word), word2Vec.getWordVector(word))
      })
    })
  }
}
