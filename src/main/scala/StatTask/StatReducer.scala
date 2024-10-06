package StatTask

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import org.slf4j.{Logger, LoggerFactory}

import scala.jdk.CollectionConverters._

class StatReducer extends Reducer[Text, LongWritable, Text, Text] {
  private var vocabulary = 0  // shared variable for computing the vocabulary of the entire dataset
  private val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)
  override def reduce(key: Text, values: java.lang.Iterable[LongWritable], context: Reducer[Text, LongWritable, Text, Text]#Context): Unit = {
    val sum = values.asScala.foldLeft(0L)((acc, value) => acc + value.get())
    vocabulary += 1 // increment vocabulary for each key
    context.write(null, new Text(s"$key,$sum"))
  }

  override def cleanup(context: Reducer[Text, LongWritable, Text, Text]#Context): Unit = {
    logger.info("Emitting Vocabulary")
    context.write(null, new Text(s"Vocabulary Size,$vocabulary")) // after all keys are processed emit the vocabulary
  }
}
