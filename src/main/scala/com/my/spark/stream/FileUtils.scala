package com.my.spark.stream

import java.io.{BufferedWriter, FileWriter}

import scala.io.Source

object FileUtils {
  def read(): Int = {
    // 指定文件路径
    val filePath = "D:\\tmp\\file.txt"
    var sum = 0;

    // 使用 Source 读取文件内容
    val source = Source.fromFile(filePath)

    try {
      // 一次性读取整个文件内容为一个字符串
      val content = source.getLines().mkString("\n")
      sum = content.toInt
    } finally {
      // 确保资源被释放
      source.close()
    }
    sum;
  }
  def write(sum:Int): Unit = {
    // 指定文件路径
    val filePath = "D:\\tmp\\file.txt"
    // 使用 BufferedWriter 写入文件
    val writer = new BufferedWriter(new FileWriter(filePath))
    try {
      writer.write(sum + "")
    } finally {
      // 确保资源被释放
      writer.close()
    }
  }
}
