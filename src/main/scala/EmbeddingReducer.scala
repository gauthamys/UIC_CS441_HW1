import org.apache.hadoop.io.{BytesWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.nativeblas.Nd4jBlas

import java.io.ByteArrayInputStream
import java.lang
import scala.jdk.CollectionConverters._

class EmbeddingReducer extends Reducer[Text, Array[Double], Text, Array[Double]]{
  override def reduce(key: Text, values: lang.Iterable[Array[Double]], context: Reducer[Text, Array[Double], Text, Array[Double]]#Context): Unit = {
    values.forEach(value => {
      context.write(key, value)
    })
  }
}
