import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

import java.lang
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class EmbeddingReducer extends Reducer[Text, Array[Double], Text, Array[Double]]{
  private def averageWordVectors(wordVectors: ListBuffer[INDArray]): Option[INDArray] = {
    if (wordVectors.isEmpty) return None  // Return None if the list is empty
    // Initialize a zero vector with the same shape as the first word vector
    val vectorSize = wordVectors.head.shape()(0)
    val sum: INDArray = Nd4j.zeros(vectorSize)

    wordVectors.foreach { wordVector =>
      sum.addi(wordVector)  // Accumulate the word vectors
    }
    // Calculate the average vector
    Some(sum.div(wordVectors.length))
  }
  override def reduce(key: Text, values: lang.Iterable[Array[Double]], context: Reducer[Text, Array[Double], Text, Array[Double]]#Context): Unit = {
    val vecs = new ListBuffer[INDArray]
    values.asScala.foreach(value => {
      vecs += Nd4j.create(value)
    })
    val res = averageWordVectors(vecs)
    context.write(key, res.get.toDoubleVector)
  }
}
