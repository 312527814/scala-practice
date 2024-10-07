package com.my

import java.util

import org.apache.spark.deploy.master.WorkerInfo
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable.HashSet

object Test2 {
  def main(args: Array[String]): Unit = {
    val colors = Map("red" -> "#FF0000",
      "azure" -> "#F0FFFF",
      "peru" -> "#CD853F")
    val seq = colors.map {
      case (id, key) => (id,key)
    }

    for (elem <- seq) {
      println(elem)
    }




    val ints:List[Int] = List(1, 2, 3)
    var stage:Seq[Int]=ints
    stage match {
      case s: List[Int] =>
        print(1)
      case s: Set[Int] =>
        print(2)
      case _=>print(3)
    }


    var  list:Seq[Int]=1 to 10
    for (element <- list) {
      println(element)
    }
    val map1 = list.map { id => (id, 1) }.toMap

    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("sort")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    println()

    //PV,UV
    //需求：根据数据计算各网站的PV,UV，同时，只显示top5
    //解题：要按PV值，或者UV值排序，取前5名

    val file: RDD[String] = sc.textFile("data/pvuvdata",5)

    //pv：
    //  187.144.73.116	浙江	2018-11-12	1542011090255	3079709729743411785	www.jd.com	Comment
    println("----------PV:-----------")

    val pair: RDD[(String, Int)] = file.map(line=>  (line.split(" ")(5),1)    )

    val reduce: RDD[(String, Int)] = pair.reduceByKey(_+_)
    val map: RDD[(Int, String)] = reduce.map(_.swap)
    val sorted: RDD[(Int, String)] = map.sortByKey(false)
    val res: RDD[(String, Int)] = sorted.map(_.swap)
    val pv: Array[(String, Int)] = res.take(5)
    pv.foreach(println)




    Thread.sleep(Long.MaxValue)

  }

  def receive: PartialFunction[Any, Unit] = {
    case e => println(s"Received unexpected message: $e")
  }
}

class  aa(a:Int,b:String){

   var c:Int=a
}
