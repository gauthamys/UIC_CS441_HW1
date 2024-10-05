package CosineSimilarity

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer

import java.lang
import scala.collection.mutable.ListBuffer

class CosSimReducer extends Reducer[Text, Text, Text, Text]{
  override def reduce(key: Text, values: lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    val candidates = new ListBuffer[(String, Double)]
    values.forEach(value => {
      val arr = value.toString.split(",")
      val w2 = arr(0)
      val sim = arr(1)
      candidates += (w2, sim)
    })
    val sortedArray = candidates.sortBy(-_._2)
    var resText = ""
    for(i <- 0 until 5) {
      resText += candidates(i)._1 + ","
    }
    context.write(key, new Text(resText))
  }
}
