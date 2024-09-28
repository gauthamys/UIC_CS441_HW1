package embeddingtask

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Reducer

class EmbeddingReducer extends Reducer[Text, LongWritable, LongWritable, Text]{
  override def reduce(key: Text, values: java.lang.Iterable[LongWritable], context: Reducer[Text, LongWritable, Text, Text]#Context): Unit = {
    context.write(null, new Text("1"))
  }
}
