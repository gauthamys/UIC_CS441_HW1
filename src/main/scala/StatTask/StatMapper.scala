package StatTask

import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.EncodingType
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

import util.StrUtil


class StatMapper extends Mapper[LongWritable, Text, Text, LongWritable] {
  private val one = new LongWritable(1)
  private val registry = Encodings.newDefaultEncodingRegistry()
  private val encoding = registry.getEncoding(EncodingType.CL100K_BASE)
  private val util = new StrUtil()

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, LongWritable]#Context): Unit = {
    val words = util.cleanTextToWords(value)
    words.foreach(word => {
      context.write(new Text(s"$word,${encoding.encode(word).toArray.mkString("[", ",", "]")}"), one)
    })
  }
}
