import com.knuddels.jtokkit.api.{EncodingType, ModelType}
import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import com.knuddels.jtokkit.Encodings

class StatMapper extends Mapper[LongWritable, Text, Text, LongWritable] {
  private val word = new IntWritable()
  private val one = new LongWritable(1)
  private val registry = Encodings.newDefaultEncodingRegistry()
  private val encoding = registry.getEncoding(EncodingType.CL100K_BASE)

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, LongWritable]#Context): Unit = {
    // Split each line into words
    val strValue = value.toString
    var cleanedText = strValue.replaceAll("[().,?!:;\"'~`{}><+=^%$/#@*-]", "").toLowerCase
    cleanedText = cleanedText.replace("[", "")
    cleanedText = cleanedText.replace("]", "")
    val tokens = cleanedText.split("\\s+")

    tokens.foreach { token =>
      val bpeTokens = encoding.encode(token)
      bpeTokens.toArray.foreach { bpeToken =>
        context.write(new Text(bpeToken.toString), one)
      }
    }
  }

}
