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
    val statJob = Job.getInstance(conf, "Word Count")

    statJob.setJarByClass(Main.getClass)
    statJob.setMapOutputKeyClass(classOf[Text])
    statJob.setMapOutputValueClass(classOf[LongWritable])

    statJob.setMapperClass(classOf[StatMapper])
    statJob.setReducerClass(classOf[StatReducer])

    statJob.setMapOutputKeyClass(classOf[Text])
    statJob.setMapOutputValueClass(classOf[LongWritable])

    FileInputFormat.addInputPath(statJob, new Path(args(0)))
    FileOutputFormat.setOutputPath(statJob, new Path(args(1)))
    System.exit(if (statJob.waitForCompletion(true)) 0 else 1)
  }
}
