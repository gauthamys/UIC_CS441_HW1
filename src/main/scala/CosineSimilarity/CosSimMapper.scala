package CosineSimilarity

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class CosSimMapper extends Mapper[LongWritable, Text, Text, Text]{
  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {

  }
}
