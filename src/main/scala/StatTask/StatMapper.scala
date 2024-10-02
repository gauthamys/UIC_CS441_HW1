package StatTask

import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.EncodingType
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class StatMapper extends Mapper[LongWritable, Text, Text, LongWritable] {

  // Define a constant value for the output
  private val one = new LongWritable(1)
  private val registry = Encodings.newDefaultEncodingRegistry()
  private val encoding = registry.getEncoding(EncodingType.CL100K_BASE)

  private def cleanText(v: Text): Array[String] = {
    var clean = v.toString.trim.toLowerCase.replaceAll("[,./?\"{}()~@!#$%^&*:;0-9']", "")
    clean = clean.replace("[", "")
    clean = clean.replace("]", "")
    val words = clean.split("\\s+")
    words
  }
  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, LongWritable]#Context): Unit = {
    val words = cleanText(value)
    words.foreach(word => {
      context.write(new Text(s"$word,${encoding.encode(word).toArray.mkString("[", ",", "]")}"), one)
    })
  }
}
