package EmbeddingTask

import org.apache.hadoop.io.{ArrayWritable, DoubleWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

import java.lang
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class EmbeddingReducer extends Reducer[Text, ArrayWritable, Text, ArrayWritable] {

  // Helper function to average word vectors
  private def averageWordVectors(wordVectors: ListBuffer[INDArray]): Option[INDArray] = {
    if (wordVectors.isEmpty) return None

    val vectorSize = wordVectors.head.shape()(0)
    val sum: INDArray = Nd4j.zeros(vectorSize)

    wordVectors.foreach { wordVector =>
      sum.addi(wordVector)
    }

    Some(sum.div(wordVectors.length))
  }

  override def reduce(key: Text, values: lang.Iterable[ArrayWritable], context: Reducer[Text, ArrayWritable, Text, ArrayWritable]#Context): Unit = {
    val vectors = new ListBuffer[INDArray]

    // Iterate over the input values (ArrayWritable) and convert them back to INDArray
    values.asScala.foreach(value => {
      // Extract the double[] from ArrayWritable and create an INDArray
      val doubleArray = value.get.map(_.asInstanceOf[DoubleWritable].get).map(_.toDouble)
      val vector = Nd4j.create(doubleArray)
      vectors += vector
    })

    // Calculate the average of the vectors
    val averageVectorOpt = averageWordVectors(vectors)

    // Write the result to context if we have a valid average vector
    averageVectorOpt.foreach { averageVector =>
      // Convert the INDArray back to ArrayWritable (DoubleWritable[])
      val writableArray = new ArrayWritable(classOf[DoubleWritable],
        averageVector.toDoubleVector.map(new DoubleWritable(_)))

      // Write the key and the average vector as output
      context.write(key, writableArray)
    }
  }
}
