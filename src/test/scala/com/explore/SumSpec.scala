package com.explore

import org.specs2.Specification

object SumSpec extends Specification {
  override def is = "Oh well let's sum stuff".title ^ s2"""
        Sum of zeros is zero $sumOfZeros
        Sum of zero and a non-zero number is that number $sumZeroAndANumber
        2 + 2 equals 4 $sum
      """

  def sumOfZeros = Sum(0, 0) must_== 0
  def sumZeroAndANumber = Sum(0, 1) must_== 1
  def sum = Sum(2, 2) must_== 4
}