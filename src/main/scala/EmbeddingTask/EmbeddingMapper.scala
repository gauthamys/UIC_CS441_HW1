package EmbeddingTask

import org.apache.hadoop.io.{ArrayWritable, DoubleWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory
import org.slf4j.{Logger, LoggerFactory}

import scala.jdk.CollectionConverters._

class EmbeddingMapper extends Mapper[LongWritable, Text, Text, ArrayWritable] {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)
  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, ArrayWritable]#Context): Unit = {
    val sentences = value.toString.split("\n").toList.asJava
    val t = new DefaultTokenizerFactory()
    t.setTokenPreProcessor(new CommonPreprocessor())
    val iter = new CollectionSentenceIterator(sentences)

    val word2Vec = new Word2Vec.Builder()
      .minWordFrequency(1)
      .tokenizerFactory(t)
      .iterate(iter)
      .layerSize(100)
      .windowSize(5)
      .tokenizerFactory(t)
      .seed(42)
      .build()

    word2Vec.fit()

    sentences.forEach(sentence => {
      sentence.split("\\s").foreach(word => {
        val wordVector = word2Vec.getWordVector(word)
        // Convert the word vector to a Hadoop-compatible type (ArrayWritable)
        val writableArray = new ArrayWritable(classOf[DoubleWritable],
          wordVector.map(v => new DoubleWritable(v)))
        context.write(new Text(word), writableArray)
      })
    })

  }
}
