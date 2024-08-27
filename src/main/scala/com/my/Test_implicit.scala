package com.my

class Test_implicit(test:Test){

  def sum(a: Int, b: Int): Int = {
    test.test()
    a + b
  }

}
