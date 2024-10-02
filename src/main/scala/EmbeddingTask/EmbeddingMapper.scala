package EmbeddingTask

import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.{EncodingType, IntArrayList}
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

import util.StrUtil
import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class EmbeddingMapper extends Mapper[LongWritable, Text, Text, Text] {
  private val registry = Encodings.newDefaultEncodingRegistry()
  private val encoding = registry.getEncoding(EncodingType.CL100K_BASE)
  private val util = new StrUtil()

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    val longSentence = new ListBuffer[String].asJava
    val words = util.cleanTextToWords(value)
    val res = words.map(word => {
      val encoded = encoding.encode(word)
      util.encoding2String(encoded)
    }).mkString(" ")
    longSentence.add(res)
    //context.write(key, new Text(res))
    val word2Vec = new Word2Vec.Builder()
      .minWordFrequency(1)
      .tokenizerFactory(new DefaultTokenizerFactory())
      .iterate(new CollectionSentenceIterator(longSentence))
      .layerSize(100)
      .windowSize(5)
      .seed(42)
      .build()
    word2Vec.fit()
    word2Vec.getVocab.words().forEach(word => {
      val vec = word2Vec.getWordVector(word)
      context.write(new Text(encoding.decode(util.decodeEncoding2String(word))), new Text(vec.mkString(",")))
    })
  }

}
