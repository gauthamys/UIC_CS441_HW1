import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.{EncodingType, IntArrayList}
import org.apache.hadoop.io
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Reducer

import scala.collection.mutable

class StatReducer extends Reducer[Text, LongWritable, Text, Text] {
  private val registry = Encodings.newDefaultEncodingRegistry()
  private val encoding = registry.getEncoding(EncodingType.CL100K_BASE)
  private val vocabulary = new mutable.HashSet[String]()
  override def reduce(key: Text, values: java.lang.Iterable[LongWritable], context: Reducer[Text, LongWritable, Text, Text]#Context): Unit = {
    var sum = 0L
    val keys = new IntArrayList()
    val sumText = new Text()
    val keyText = new Text()
    val iter = values.iterator()
    while (iter.hasNext) {
      sum = sum + iter.next().get()
    }
    vocabulary += key.toString
    keys.add(key.toString.toInt)
    sumText.set(sum.toString)
    keyText.set(encoding.decode(keys))

    val resText = new Text()
    resText.set(key.toString + "," + keyText.toString + "," + sum.toString)

    context.write(null, resText)
  }
  override def cleanup(context: Reducer[Text, LongWritable, Text, Text]#Context): Unit = {
    // After processing all words, output the vocabulary size
    context.write(null, new Text(s"Vocabulary Size,${vocabulary.size}"))
  }
}
