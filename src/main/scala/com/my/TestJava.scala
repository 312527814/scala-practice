package com.my

object TestJava {
  def main(args: Array[String]): Unit = {
    val java = new TestJava()
    java.test()
  }
}

class TestJava {
  var java = new MyTestJava()

  def test(): Unit = {
    var java: MyTestJava = new MyTestJava()
    java.test();
  }
}