package CosineSimilarity

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer

import java.lang

class CosSimReducer extends Reducer[Text, Text, Text, Text]{
  override def reduce(key: Text, values: lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    values.forEach(value => {
      context.write(key, value)
    })
  }
}
