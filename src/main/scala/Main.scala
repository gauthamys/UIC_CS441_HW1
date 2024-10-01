import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length != 3) {
      System.err.println("Usage: <\"embedding\" or \"stat\"> <input path> <output path>")
      System.exit(-1)
    }
    val jobName = args(0)
    val conf = new Configuration
    val job = Job.getInstance(conf, "Token Encoding & Word Count")
    job.setJarByClass(Main.getClass)
    if (jobName == "stat") {
      // Set the Mapper and Reducer classes
      job.setMapOutputKeyClass(classOf[Text])
      job.setMapOutputValueClass(classOf[LongWritable])
      job.setMapperClass(classOf[StatMapper])
      job.setReducerClass(classOf[StatReducer])
    }
    else if (jobName == "embedding") {
      // Set the Mapper and Reducer classes
      job.setMapOutputKeyClass(classOf[Text])
      job.setMapOutputValueClass(classOf[LongWritable])
      job.setMapperClass(classOf[EmbeddingMapper])
      job.setReducerClass(classOf[EmbeddingReducer])
    }

    // Input and Output paths
    FileInputFormat.addInputPath(job, new Path(args(1)))
    FileOutputFormat.setOutputPath(job, new Path(args(2)))

    // Submit the job and wait for completion
    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}
