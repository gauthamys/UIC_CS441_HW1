import EmbeddingTask.{EmbeddingMapper, EmbeddingReducer}
import StatTask.{StatMapper, StatReducer}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{ArrayWritable, IntWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.{FileInputFormat, TextInputFormat}
import org.apache.hadoop.mapreduce.lib.output.{FileOutputFormat, TextOutputFormat}

object Main {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration
    val job = Job.getInstance(conf, "Word Count & Embedding")
    job.setJarByClass(Main.getClass)

    val jobName = args(0)

    if(jobName == "embedding") {
      job.setMapOutputKeyClass(classOf[Text])
      job.setMapOutputValueClass(classOf[Text])

      job.setMapperClass(classOf[EmbeddingMapper])
      job.setReducerClass(classOf[EmbeddingReducer])
    }
    else if (jobName == "stat") {
      job.setMapOutputKeyClass(classOf[Text])
      job.setMapOutputValueClass(classOf[LongWritable])

      job.setMapperClass(classOf[StatMapper])
      job.setReducerClass(classOf[StatReducer])

      job.setMapOutputKeyClass(classOf[Text])
      job.setMapOutputValueClass(classOf[LongWritable])

    }
    FileInputFormat.addInputPath(job, new Path(args(1)))
    FileOutputFormat.setOutputPath(job, new Path(args(2)))
    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}
