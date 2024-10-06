import CosineSimilarity.{CosSimMapper, CosSimReducer}
import EmbeddingTask.{EmbeddingMapper, EmbeddingReducer}
import StatTask.{StatMapper, StatReducer}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.slf4j.{Logger, LoggerFactory}

object Main {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)
  def main(args: Array[String]): Unit = {
    val conf = new Configuration
    val job = Job.getInstance(conf, "Word Count & Embedding")
    job.setJarByClass(Main.getClass)

    val jobName = args(0)

    if(jobName == "embedding") {
      logger.info("Running embedding job ... ")
      job.setMapOutputKeyClass(classOf[Text])
      job.setMapOutputValueClass(classOf[Text])

      job.setMapperClass(classOf[EmbeddingMapper])
      job.setReducerClass(classOf[EmbeddingReducer])
    }
    else if (jobName == "stat") {
      logger.info("Running stat job")
      job.setMapOutputKeyClass(classOf[Text])
      job.setMapOutputValueClass(classOf[LongWritable])

      job.setMapperClass(classOf[StatMapper])
      job.setReducerClass(classOf[StatReducer])

    }
    else if (jobName == "cosSim") {
      logger.info("Running cosSim job")
      job.setMapOutputKeyClass(classOf[Text])
      job.setMapOutputValueClass(classOf[Text])

      job.setMapperClass(classOf[CosSimMapper])
      job.setReducerClass(classOf[CosSimReducer])

    } else {
      logger.error("Invalid task name, choose from <stat, embedding or cosSim>")
      System.exit(1)
    }
    FileInputFormat.addInputPath(job, new Path(args(1)))
    FileOutputFormat.setOutputPath(job, new Path(args(2)))
    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}
