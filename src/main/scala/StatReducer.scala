import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.{EncodingType, IntArrayList, ModelType}
import org.apache.hadoop.io
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Reducer

import scala.collection.mutable
import scala.jdk.CollectionConverters._

class StatReducer extends Reducer[Text, LongWritable, Text, Text] {
  private val registry = Encodings.newDefaultEncodingRegistry()
  private val encoding = registry.getEncoding(EncodingType.CL100K_BASE)
  private val vocabulary = new mutable.HashSet[String]()
  
  override def reduce(key: Text, values: java.lang.Iterable[LongWritable], context: Reducer[Text, LongWritable, Text, Text]#Context): Unit = {
    // sum all occurrences of the word and add the key to the vocabulary set
    val sum = values.asScala.foldLeft(0L)((acc, value) => acc + value.get())
    val keys = new IntArrayList()       // array for passing to decode function
    vocabulary += key.toString          // add the key to the vocabulary set
    keys.add(key.toString.toInt)
    context.write(null, new Text(key.toString + "," + encoding.decode(keys) + "," + sum.toString))
  }
  override def cleanup(context: Reducer[Text, LongWritable, Text, Text]#Context): Unit = {
    // After processing all words, output the vocabulary size
    context.write(null, new Text(s"Vocabulary Size,${vocabulary.size}"))
  }
}
