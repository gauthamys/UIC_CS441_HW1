package embeddingtask

import org.apache.hadoop.io
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class EmbeddingMapper extends Mapper[LongWritable, Text, Text, LongWritable] {
  private val one = new LongWritable(1)
  private val word = new Text()
  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, LongWritable]#Context): Unit = {
    context.write(null, one)
  }
}
