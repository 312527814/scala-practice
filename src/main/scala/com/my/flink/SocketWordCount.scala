package com.my.flink

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.configuration.RestOptions
object SocketWordCount {
  def main(args: Array[String]): Unit = {
    test()
  }

  def test3(): Unit ={
    //准备环境
    /**
     * createLocalEnvironment 创建一个本地执行的环境  local
     * createLocalEnvironmentWithWebUI 创建一个本地执行的环境  同时还开启Web UI的查看端口  8081
     * getExecutionEnvironment 根据你执行的环境创建上下文，比如local  cluster
     */

    //    val conf = new Configuration();
    //
    //    conf.setString(RestOptions.BIND_PORT, "8081")
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    /**
     * DataStream：一组相同类型的元素 组成的数据流
     * 如果数据源是scoket  并行度只能是1
     */
    val initStream:DataStream[String] = env.socketTextStream("192.168.16.129",8081)
    val wordStream = initStream.flatMap(_.split(" "))
    val pairStream = wordStream.map((_,1))
    val keyByStream = pairStream.keyBy(0)
    val restStream = keyByStream.reduce(
      (n1, n2) => {
        println("n1 =》 " + n1 + ",n2 => " + n2)
        (n1._1, n1._2 + n2._2)
      }).setParallelism(2)
    restStream.print()

    /**
     * 6> (msb,1)
     * 1> (,,1)
     * 3> (hello,1)
     * 3> (hello,2)
     * 6> (msb,2)
     * 默认就是有状态的计算
     * 6>  代表是哪一个线程处理的
     * 相同的数据一定是由某一个thread处理
     **/
    //启动Flink 任务
    env.execute("first flink job")
  }

  def test2(): Unit ={
    //准备环境
    /**
     * createLocalEnvironment 创建一个本地执行的环境  local
     * createLocalEnvironmentWithWebUI 创建一个本地执行的环境  同时还开启Web UI的查看端口  8081
     * getExecutionEnvironment 根据你执行的环境创建上下文，比如local  cluster
     */

//    val conf = new Configuration();
//
//    conf.setString(RestOptions.BIND_PORT, "8081")
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI()
    /**
     * DataStream：一组相同类型的元素 组成的数据流
     * 如果数据源是scoket  并行度只能是1
     */
    val initStream:DataStream[String] = env.socketTextStream("192.168.16.129",8081)
    val wordStream = initStream.flatMap(_.split(" "))
    val pairStream = wordStream.map((_,1))
    val keyByStream = pairStream.keyBy(0)
    val restStream = keyByStream.reduce(
      (n1, n2) => {
        println("n1 => " + n1 + ",n2 => " + n2)
        (n1._1, n1._2 + n2._2)
      }).setParallelism(2)
    restStream.print()

    /**
     * 6> (msb,1)
     * 1> (,,1)
     * 3> (hello,1)
     * 3> (hello,2)
     * 6> (msb,2)
     * 默认就是有状态的计算
     * 6>  代表是哪一个线程处理的
     * 相同的数据一定是由某一个thread处理
     **/
    //启动Flink 任务
    env.execute("first flink job")
  }
  def test(): Unit ={
    //准备环境
    /**
     * createLocalEnvironment 创建一个本地执行的环境  local
     * createLocalEnvironmentWithWebUI 创建一个本地执行的环境  同时还开启Web UI的查看端口  8081
     * getExecutionEnvironment 根据你执行的环境创建上下文，比如local  cluster
     */
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    /**
     * DataStream：一组相同类型的元素 组成的数据流
     * 如果数据源是scoket  并行度只能是1
     */
    val initStream:DataStream[String] = env.socketTextStream("192.168.16.129",8081)
    val wordStream = initStream.flatMap(_.split(" "))
    val pairStream = wordStream.map((_,1))
    val keyByStream = pairStream.keyBy(0)
    val restStream = keyByStream.sum(1)
    restStream.print()

    /**
     * 6> (msb,1)
     * 1> (,,1)
     * 3> (hello,1)
     * 3> (hello,2)
     * 6> (msb,2)
     * 默认就是有状态的计算
     * 6>  代表是哪一个线程处理的
     * 相同的数据一定是由某一个thread处理
     **/
    //启动Flink 任务
    env.execute("first flink job")
  }

}
