package stattask

import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.{EncodingType, ModelType}
import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class StatMapper extends Mapper[LongWritable, Text, Text, LongWritable] {
  private val word = new IntWritable()
  private val one = new LongWritable(1)
  private val registry = Encodings.newDefaultEncodingRegistry()
  private val encoding = registry.getEncodingForModel(ModelType.GPT_4)
  // private val logger = new Logger()

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, LongWritable]#Context): Unit = {
    // Clean each line - remove punctuation, convert all words to lowercase and map <wordEncoding, 1>
    val strValue = value.toString

    // logger.info("Cleaning up input text")
    var cleanedText = strValue.trim.replaceAll("[().,?!:;\"'{}%$/<>+-]", " ").toLowerCase
    cleanedText = cleanedText.replaceAll("[0-9]", "")
    cleanedText = cleanedText.replace("[", "")
    cleanedText = cleanedText.replace("]", "")
    cleanedText = cleanedText.replace("\n", " ")

    // logger.info("Splitting Tokens")
    val tokens = cleanedText.split("\\s+")

    // logger.info("Creating Byte Pair Encodings")
    tokens.foreach { token =>
      val bpeTokens = encoding.encode(token)
      bpeTokens.toArray.foreach { bpeToken =>
        context.write(new Text(bpeToken.toString), one)
      }
    }

  }

}
