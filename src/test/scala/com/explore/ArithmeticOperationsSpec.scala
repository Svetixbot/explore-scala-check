package com.explore

import org.scalacheck.Prop.{collect, forAll}
import org.scalacheck.Properties

object ArithmeticOperationsSpec extends Properties("Sum") {
  property("sum") = forAll{ (x: Int, y: Int) ⇒
    collect((x, y)){
      Sum(x, y) == x + y
    }
  }
}