package StatTask

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Reducer

import scala.jdk.CollectionConverters._

class StatReducer extends Reducer[Text, LongWritable, Text, Text] {
  private var vocabulary = 0

  override def reduce(key: Text, values: java.lang.Iterable[LongWritable], context: Reducer[Text, LongWritable, Text, Text]#Context): Unit = {
    val sum = values.asScala.foldLeft(0L)((acc, value) => acc + value.get())
    vocabulary += 1
    context.write(null, new Text(s"$key,$sum"))
  }

  override def cleanup(context: Reducer[Text, LongWritable, Text, Text]#Context): Unit = {
    context.write(null, new Text(s"Vocabulary Size,$vocabulary"))
  }
}
