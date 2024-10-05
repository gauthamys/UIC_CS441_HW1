package CosineSimilarity

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class CosSimMapper extends Mapper[LongWritable, Text, Text, Text]{
  private def convertWordVecToPair(s: String): (String, Array[Double]) = {
    val sn = s.replace("[", "").replace("]", "")
    (sn.split(":")(0), sn.split(":")(1).split(",").map(n => n.toDouble))
  }
  private def similarity(v1: String, v2: String): ((String, String), Double) = {
    val (w1, vec1) = convertWordVecToPair(v1)
    val (w2, vec2) = convertWordVecToPair(v2)
    val modVec1 = math.sqrt(vec1.map(x => x * x).sum)
    val modVec2 = math.sqrt(vec2.map(x => x * x).sum)
    ((w1, w2), vec1.zip(vec2).map { case (a, b) => a * b }.sum / (modVec1 * modVec2))
  }
  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    val wordVectors = value.toString.split("%")
    for(i <- 0 until wordVectors.length) {
      for(j <- i + 1 until wordVectors.length) {
        val ((w1, w2),sim) = similarity(wordVectors(i), wordVectors(j))
        if(sim > 0.4) { // config
          context.write(new Text(w1), new Text(s"$w2,$sim"))
        }
      }
    }
  }
}
