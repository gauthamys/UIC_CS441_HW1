import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

object WordCountJob {
  def main(args: Array[String]): Unit = {
    // Hadoop Configuration
    val conf = new Configuration()
    val job = Job.getInstance(conf, "Word Count")

    job.setJarByClass(getClass)

    // Set Mapper and Reducer
    job.setMapperClass(classOf[WordCountMapper])
    job.setReducerClass(classOf[WordCountReducer])

    // Set output key and value types
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[LongWritable])

    // Input and Output paths
    FileInputFormat.addInputPath(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))

    // Exit when the job completes
    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}
