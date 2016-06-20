# Property based testing
## with Scala and ScalaCheck

First, let's define **example based testing**

```scala
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
```

> This is something we all comfortable with. This is how we started to learn about TDD.
> We start from the most simple test case and build up on it by providing more and more examples:
> `0 + 0` must be `0`, `1 + 0` must be `1`, `2 + 2` must be `4`

Now, let's get out of the comfort zone and see what is ScalaCheck and **property based testing**

```scala
  property("sum") = forAll{ (x: Int, y: Int) ⇒
      Sum(x, y) == x + y
  }
```
Once we run this test, we will see this
```zsh
> test
[info] + Sum.sum: OK, passed 100 tests.
[info] Passed: Total 1, Failed 0, Errors 0, Passed 1
[success] Total time: 1 s, completed 21/06/2016 8:07:29 AM
```

Hey hey, but what's happened actually? what did we just test? 
With scalacheck you can easily classify and collect generated results to validate the input.

```scala
  property("sum") = forAll{ (x: Int, y: Int) ⇒
    collect((x, y)){
      Sum(x, y) == x + y
    }
  }
```

```zsh
> test
[info] Compiling 1 Scala source to /Users/sfilimon/Projects/explore-scala-check/target/scala-2.11/test-classes...
[info] + Sum.sum: OK, passed 100 tests.
[info] > Collected test data:
[info] 5% (1,-2147483648)
[info] 2% (-1,0)
[info] 2% (2147483647,1)
[info] 2% (-2147483648,0)
[info] 2% (0,-2147483648)
[info] 2% (-1,2147483647)
[info] 1% (1563274363,-1)
[info] 1% (1652717671,1259620524)
[info] 1% (750274819,1)
[info] 1% (1372257998,2147483647)
[info] 1% (-2147483648,1651342388)
[info] 1% (2147483647,2147483647)
[info] 1% (-697823930,904480843)
[info] 1% (-888053844,1988242462)
...
```


