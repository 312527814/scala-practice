package com.my.spark.stream

import java.io.{BufferedWriter, FileWriter}
import java.util.Date

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.io.Source

object lesson02_receiver02_custorm {


  def main(args: Array[String]): Unit = {
    test3()
  }

  /**
   * 基于第三方有状态化操作
   */
  def test3(){

    val conf: SparkConf = new SparkConf().setMaster("local[10]").setAppName("receiver")
    val ssc = new StreamingContext(conf, Seconds(2))
    ssc.sparkContext.setLogLevel("ERROR")
    val dstream: ReceiverInputDStream[String] = ssc.receiverStream(new CustormReceiver("localhost", 8889))
    val dsWind = dstream.window(Seconds(6), Seconds(4))
    val dsWindMap = dsWind.transform(rdd => {
      val value: RDD[(String, Int)] = rdd.map(_.split(" ")).map(m => (m(0), 1)).reduceByKey((nv, ov) => {
        ov + nv
      })
      value.map(m => {
        val sum = FileUtils.read()
        FileUtils.write(sum + m._2)
//        println("sum " + sum)
        (m._1, sum)
      })
    })
    dsWindMap.print()

    ssc.start()
    ssc.awaitTermination()
  }


  /**
   * 基于sparkStream自己的有状态化操作
   */
  def test2(){
    val conf: SparkConf = new SparkConf().setMaster("local[10]").setAppName("receiver")
    val ssc = new StreamingContext(conf, Seconds(2))
    ssc.sparkContext.setLogLevel("ERROR")
    ssc.sparkContext.setCheckpointDir(".")
    val dstream: ReceiverInputDStream[String] = ssc.receiverStream(new CustormReceiver("localhost", 8889))
    val dsWind = dstream.window(Seconds(6), Seconds(4))
    val dsWindMap = dsWind.transform(rdd => {
      val value:RDD[(String,Int)] = rdd.map(_.split(" ")).map(m => (m(0), m(1).toInt))
      value
    })
    val dsWindMapByKey:DStream[(String,Int)] = dsWindMap.updateStateByKey(
      (nv: Seq[Int], ov: Option[Int]) => {

       /* println("..........nv..begin.....................")
        nv.foreach(f => {
          print(f + ",")
        })
        println("..........nv..end.....................")

        println("..........ov..begin.....................")
        ov.foreach(f => {
          print(f + ",")
        })
        println("..........nv..end.....................")*/
        //每个批次的job里  对着nv求和
        val count: Int = nv.count(_ > 0)
        val oldVal: Int = ov.getOrElse(0)
        Some(count + oldVal)
      }
    )
//    dsWindMapByKey.foreachRDD(rdd => rdd)
    dsWindMapByKey.print()
    ssc.start()
    ssc.awaitTermination()
  }

  def test1(){
    val conf: SparkConf = new SparkConf().setMaster("local[10]").setAppName("receiver")
    //local[n]  2个就够了：
    // 1个给receiverjob的task，
    // 另一个给beatch计算的job（只不过如果batch比较大，你期望n>2,因为多出来的线程可以跑并行的batch@job@task）

    //微批的流式计算，时间去定义批次 （while->时间间隔触发job）
    val ssc = new StreamingContext(conf, Seconds(2))
    ssc.sparkContext.setLogLevel("ERROR")
    val dstream: ReceiverInputDStream[String] = ssc.receiverStream(new CustormReceiver("localhost", 8889))
    val dsWind = dstream.window(Seconds(6), Seconds(4))
    val dsWindMap:DStream[(String,Int)] = dsWind.transform(rdd => {

      println("............begin.....................")

      val sortRdd:RDD[(String,Int)] = rdd.map(_.split(" ")).map(m => (m(0), m(1).toInt)).sortBy(s => s._2, false)
      sortRdd.collect().foreach(f => {
        val s = Thread.currentThread().getName + " key:" + f._1 + " value:" + f._2
        println(s)
      })

      println("............end.....................")
      sortRdd
    })
    //    dsWindMap.saveAsTextFiles("aw","txt")
    dsWindMap.foreachRDD(rdd => rdd)
    //    dsWindMap.print()
    ssc.start()
    ssc.awaitTermination()
  }

}
