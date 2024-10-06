package CosineSimilarity

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import org.slf4j.{Logger, LoggerFactory}
import java.lang
import scala.collection.mutable.ListBuffer

class CosSimReducer extends Reducer[Text, Text, Text, Text]{
  private val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)
  override def reduce(key: Text, values: lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    val candidates = new ListBuffer[(String, Double)]
    values.forEach(value => {
      val arr = value.toString.split(",")
      val w2 = arr(0)
      val sim = arr(1)
      candidates.addOne((w2, sim.toDouble))
    })
    logger.info("sorting the similarities")
    val sortedArray = candidates.sortBy(-_._2)
    var resText = ""
    if(candidates.nonEmpty) {
      resText += sortedArray.head._1
    }
    context.write(key, new Text(resText))
  }
}
