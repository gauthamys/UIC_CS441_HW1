package EmbeddingTask

import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.EncodingType
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class EmbeddingMapper extends Mapper[LongWritable, Text, Text, Text] {
  private val registry = Encodings.newDefaultEncodingRegistry()
  private val encoding = registry.getEncoding(EncodingType.CL100K_BASE)

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    val sentences = value.toString.split("\n")
    val encodedSentences = new ListBuffer[String]
    sentences.foreach(sentence => {
      var encodedSentence = " "
      sentence.split("\\s+").foreach(word => {
        encodedSentence += encoding.encode(word).toArray.mkString("[", ",", "] ")
      })
      encodedSentences += encodedSentence
    })

    val t = new DefaultTokenizerFactory()
    t.setTokenPreProcessor(new CommonPreprocessor())
    val iter = new CollectionSentenceIterator(encodedSentences.toList.asJavaCollection)
    val iterRawSentence = new CollectionSentenceIterator(sentences.toList.asJavaCollection)
    val word2Vec = new Word2Vec.Builder()
      .minWordFrequency(1)
      .tokenizerFactory(t)
      .iterate(iterRawSentence)
      .layerSize(100)
      .windowSize(5)
      .tokenizerFactory(t)
      .seed(42)
      .build()
    word2Vec.fit()

    value.toString.split("\\s+").foreach(word => {
      val queryToken = encoding.encode(word).toArray.mkString("[", ",", "]")
      if(word2Vec.hasWord(word)) {
        val vec = word2Vec.getWordVector(word).mkString("[", ",", "]")
        context.write(new Text(s"$word,$queryToken"), new Text(vec))
      }
    })
  }
}
