package EmbeddingTask

import org.apache.hadoop.io.{ArrayWritable, DoubleWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

import java.lang
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class EmbeddingReducer extends Reducer[Text, Text, Text, Text] {
  override def reduce(key: Text, values: lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    values.forEach(value => {
      context.write(key, value)
    })
  }
}
