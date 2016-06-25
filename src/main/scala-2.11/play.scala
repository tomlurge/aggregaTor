import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object play {
  def main(args : Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("GitHubPushCounter")
      .setMaster("local[*]")

    val sc = new SparkContext(conf)
    // println(sc)

    val sq = new SQLContext(sc)

    val homeDir = System.getenv("HOME")
    val inputPath = homeDir + "/temp/2015-01-01-0.json"
    val gitLog = sq.read.json(inputPath)
    val pushes = gitLog.filter("type = 'PushEvent'")
    println(inputPath)
    println(sc)

    pushes.printSchema
    println("all events: " + gitLog.count)
    println("only pushes: " + pushes.count)
    pushes.show(5)

  }
  Console.println("yo!")
  System.out.println("ha!")

}
