package EmbeddingTask

import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.EncodingType
import com.typesafe.config.ConfigFactory
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

import util.StrUtil
import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class EmbeddingMapper extends Mapper[LongWritable, Text, Text, Text] {
  private val registry = Encodings.newDefaultEncodingRegistry()
  private val encoding = registry.getEncoding(EncodingType.CL100K_BASE)
  private val util = new StrUtil()
  private val conf = ConfigFactory.load()
  private val seed = conf.getInt("Embedding.seed")
  private val layerSize = conf.getInt("Embedding.layerSize")
  private val windowSize = conf.getInt("Embedding.windowSize")
  private val minWordFrequency = conf.getInt("Embedding.minWordFrequency")

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    val longSentence = new ListBuffer[String].asJava
    val words = util.cleanTextToWords(value)
    val res = words.map(word => {
      val encoded = encoding.encode(word)
      util.encoding2String(encoded)
    }).mkString(" ")
    longSentence.add(res)

    // train the Word2Vec model
    val word2Vec = new Word2Vec.Builder()
      .minWordFrequency(minWordFrequency)
      .tokenizerFactory(new DefaultTokenizerFactory())
      .iterate(new CollectionSentenceIterator(longSentence))
      .layerSize(layerSize)
      .windowSize(windowSize)
      .seed(seed)
      .build()
    word2Vec.fit()

    // For every word in the vocabulary emit the learnt embedding
    word2Vec.getVocab.words().forEach(word => {
      val vec = word2Vec.getWordVector(word)
      context.write(new Text(encoding.decode(util.decodeEncoding2String(word))), new Text(vec.mkString(",")))
    })
  }

}
