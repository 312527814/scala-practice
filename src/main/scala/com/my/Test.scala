package com.my

object Test {

  //_: 当匿名函数作为传参时，如果入参只出现一次，则参数可省且 ，可以用占位符 _ 代替。


  def computer(a: Int, b: Int, f: (Int, Int) => Int): Unit = {
    val res: Int = f(a, b)
    println(res)
  }

  computer(1, 2, _ + _)

  //_:部分应用的函数,如果部分函数完全等于应用函数可以写成 mult _
  // 如果只等于部分  mult(_:Int,2,_:Int)
  val part = computer(_: Int, 1, _: (Int, Int) => Int)
  part(2, _ * _)

  def main(args: Array[String]): Unit = {

    val test = new Test(3,5)

    val (rpcEnv, a, _) = test.startRpcEnvAndEndpoint("2")
    println("rpcEnv " + rpcEnv + "" + a)

    /* {
       val i = test1(2, _ * 2 + _)
       println(i)
     }*/

    /*{
      val add: (Int, Int) => Int = (a, b) => a + b
      val a = sum _
      val ia1 = a(1, 2, 3)
      println(ia1)

      val ia2 = a.apply(1, 2, 3)
      println(ia2)

      val b=sum(1,_:Int,3)
      val ib1 = b.apply(2)
      println(ib1)

      val c = sum(_: Int, _: Int, _: Int)
      var ic1 = c(1, 2, 3)
      println(ic1)

    }*/
    {
      val a = sum _
      println(a(1, 2, 3))

      val b = sum(_: Int, 2, 3);
      println(b(1))
    }


  }

  def sum(a: Int, b: Int, c: Int) = a + b + c

  def test1(p: Int, f: (Int, Int) => Int): Int = {
    f(p, 1)
  }

  def test6(str: Int): Long = {
    var ret: Long = 1

    // step1
    // def f(c: Char) = { ret *= c }
    // str.foreach(f)

    // step2
    //    str.foreach(c => ret *= c)

    // step3
    // str.foreach(_ => ret *= _)
    def sum(a: Int, b: Int) = a + b


    ret
  }
}

class Test() {
  var _a = 0;
  var _b = 0;

  def this(a: Int) {
    this()
  }

  def this(a: Int, b: Int) {
    this()
    _a = a
    _b = b
  }

  def test(): Unit = {
    println("a " + _a)
    println("b " + _b)

  }

  def ss(a: Int): Unit = {
    println(a)
  }

  def startRpcEnvAndEndpoint(
                              host: String
                            ): (Int, Int, String) = {

    val loop = new MessageLoop()
    loop.run();
    (1, 1, host)
  }

  private class MessageLoop {
    def run(): Unit = {
      ss(Test.this);
    }

    def ss(test: Test): Unit = {
      println(test)
    }
  }

}
