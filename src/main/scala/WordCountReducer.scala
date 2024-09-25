import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import scala.jdk.CollectionConverters._

class WordCountReducer extends Reducer[Text, LongWritable, Text, LongWritable] {
  override def reduce(key: Text, values: java.lang.Iterable[LongWritable], context: Reducer[Text, LongWritable, Text, LongWritable]#Context): Unit = {
    val sum = values.asScala.foldLeft(0L)(_ + _.get)

    // Emit (word, sum of occurrences)
    context.write(key, new LongWritable(sum))
  }
}
