import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class WordCountMapper extends Mapper[LongWritable, Text, Text, LongWritable] {
  private val word = new Text()
  private val one = new LongWritable(1)

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, LongWritable]#Context): Unit = {
    // Split each line into words
    val tokens = {
      value.toString.split("\\s+")
    }

    // Emit each word as (word, 1)
    for (token <- tokens) {
      word.set(token)
      context.write(word, one)
    }
  }
}
