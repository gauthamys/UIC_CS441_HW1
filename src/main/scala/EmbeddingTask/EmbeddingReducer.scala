package EmbeddingTask

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer

import java.lang
import scala.jdk.CollectionConverters._

class EmbeddingReducer extends Reducer[Text, Text, Text, Text] {
  def convertVec2Arr(v: String): Array[Double] = {
    v.split(",").map(x => x.toDouble)
  }
  override def reduce(key: Text, values: lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    val acc = new Array[Double](100)
    // average the vectors for each word
    values.asScala.foreach(value => {
      val vec = convertVec2Arr(value.toString)
      for (i <- 0 until 100) {
        acc(i) += vec(i) / 100
      }
    })
    context.write(key, new Text(acc.mkString("[", ",", "]")))
  }
}
