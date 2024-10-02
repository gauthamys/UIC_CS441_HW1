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

class EmbeddingMapper extends Mapper[LongWritable, Text, LongWritable, Text] {
  private val registry = Encodings.newDefaultEncodingRegistry()
  private val encoding = registry.getEncoding(EncodingType.CL100K_BASE)
  private val util = new StrUtil()
  private val sentence = new ListBuffer[String].asJavaCollection
  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, LongWritable, Text]#Context): Unit = {
    val words = util.cleanTextToWords(value)
    var res = ""
    if (words.length > 0) {
      res = words.map(word => {
        val encoded = encoding.encode(word)
        util.encoding2String(encoded)
      }).mkString(" ")
//      sentence.add(res)
      context.write(key, new Text(res))
    }

  }

//  override def cleanup(context: Mapper[LongWritable, Text, LongWritable, Text]#Context): Unit = {
//    val t = new DefaultTokenizerFactory()
//    t.setTokenPreProcessor(new CommonPreprocessor())
//    val word2Vec = new Word2Vec.Builder()
//      .minWordFrequency(1)
//      .tokenizerFactory(t)
//      .iterate(new CollectionSentenceIterator(sentences))
//      .layerSize(100)
//      .windowSize(5)
//      .seed(42)
//      .build()
//    word2Vec.fit()
//  }
}
