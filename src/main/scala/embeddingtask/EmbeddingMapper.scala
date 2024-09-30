package embeddingtask

import org.apache.hadoop.io
import org.apache.hadoop.io.{BytesWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory

import scala.jdk.CollectionConverters
import java.io.ByteArrayOutputStream
import java.util
import scala.jdk.CollectionConverters.*
import scala.collection.mutable.ListBuffer

class EmbeddingMapper extends Mapper[LongWritable, Text, Text, BytesWritable] {
  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, BytesWritable]#Context): Unit = {
    // Clean and tokenize the input text
    val strValue = value.toString
    val sentences : util.Collection[String] = new ListBuffer[String].asJava
    sentences.add(strValue)
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

    // Serialize the Word2Vec model into binary format
    val byteArrayOutputStream = new ByteArrayOutputStream()
    WordVectorSerializer.writeWord2VecModel(word2Vec, byteArrayOutputStream)

    // Convert the ByteArrayOutputStream to a byte array
    var modelBytes = new ByteArrayOutputStream()
    modelBytes = byteArrayOutputStream

    context.write(new Text("model"), new BytesWritable(modelBytes.toByteArray))
  }

}
