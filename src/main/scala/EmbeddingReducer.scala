import org.apache.hadoop.io.{BytesWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer

import java.io.ByteArrayInputStream
import java.lang

class EmbeddingReducer extends Reducer[Text, BytesWritable, LongWritable, BytesWritable]{
  override def reduce(key: Text, values: lang.Iterable[BytesWritable], context: Reducer[Text, BytesWritable, Text, BytesWritable]#Context): Unit = {
    val iter = values.iterator() // iterate over values
    while (iter.hasNext) {
      val w2v = WordVectorSerializer.readAsBinaryNoLineBreaks(new ByteArrayInputStream(iter.next().getBytes))
      context.write(new Text(key.toString), new BytesWritable(iter.next().getBytes))
    }
  }
}
