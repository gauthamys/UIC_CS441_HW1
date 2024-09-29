import embeddingtask.{EmbeddingMapper, EmbeddingReducer}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.{FileOutputFormat, TextOutputFormat}
import stattask.{StatMapper, StatReducer}

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length != 3) {
      System.err.println("Usage: TokenEmbeddingWordCount <input path> <output path>")
      System.exit(-1)
    }
    val jobName = args(0)
    val conf = new Configuration
    val job = Job.getInstance(conf, "Token Encoding & Word Count")
    if (jobName == "stat") {
      job.setJarByClass(Main.getClass)
      // Set the Mapper and Reducer classes
      job.setMapperClass(classOf[StatMapper])
      job.setReducerClass(classOf[StatReducer])
    }
    else if (jobName == "embedding") {
      job.setJarByClass(Main.getClass)
      // Set the Mapper and Reducer classes
      job.setMapperClass(classOf[EmbeddingMapper])
      job.setReducerClass(classOf[EmbeddingReducer])
    }

    // Set output key and value types
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[LongWritable])
    job.setOutputFormatClass(classOf[TextOutputFormat[?, ?]])

    // Input and Output paths
    FileInputFormat.addInputPath(job, new Path(args(1)))
    FileOutputFormat.setOutputPath(job, new Path(args(2)))

    // Submit the job and wait for completion
    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}
