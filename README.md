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
object SumSpec extends Specification with ScalaCheck {
  override def is = "Oh well let's sum stuff".title ^ s2"""
        Sum should sum all the integers $sum
      """

  def sum = prop {(a: Int, b: Int) ⇒
    Sum(a, b) must_==(a + b)
  }
}
```
Once we run this test, we will see this (`1 example, 100 expectations, 0 failure, 0 error`!!!)
```zsh
[info] Oh well let's sum stuff
[info]         + Sum should sum all the integers
[info]
[info] Total for specification Oh well let's sum stuff
[info] Finished in 158 ms
[info] 1 example, 100 expectations, 0 failure, 0 error
```

Hey hey, but what's happened actually? what did we just test? 
Yeh! You need to check that your generated values are meaningful. We can ask scalaCheck to collect the results:

```
[info] Oh well let's sum stuff
[info]         + Sum should sum all the integers
[info] OK, passed 100 tests.
[info] > Collected test data:
[info] 4% -2147483648, -1
[info] 4% 1, -2147483648
[info] 3% 0, 1
[info] 3% 2147483647, -2147483648
[info] 2% -1, 1
[info] 2% 0
[info] 2% 0, 2147483647
[info] 1% 1948995680, -306468807
[info] 1% 1583418934, -1
[info] 1% 1733311458, 10713653
[info] 1% 1066231135, 0
[info] 1% 1434660374, 2085422593
[info] 1% -1, 1159385015
[info] 1% 1379024821, -1552976560
[info] 1% -1453022433, 0
[info] 1% -226289656, 2147483647
...
```

## How about some real life examples?

We wrote a small utility function which maps db columns to our model. 
Sometimes [String] should be presented as [Int], if it is not a number we should return a mapping error.

Let's start with simple example based aproach. 

```
object AnormParseSpec extends Specification with ScalaCheck {
  override def is =
    s2"""AnormParse.column should:
      map column value                        $mapExample

    """
  val meta = MetaDataItem(ColumnName("foo", None), false, "Foo")

  val arbValidIntString = Arbitrary[String](Gen.chooseNum(Int.MinValue, Int.MaxValue).map(_.toString))
  
  def mapExample = {
    column[String, Int]((s: String) => s.toInt).apply("123", meta).toEither must beRight(123)
  }
}
```

Now let's rewrite it with scalacheck generators.

```
object AnormParseSpec extends Specification with ScalaCheck {
  override def is =
    s2"""AnormParse.column should:
      map column value           $map
    """
  
  val meta = MetaDataItem(ColumnName("foo", None), false, "Foo")
  
  val arbValidIntString = Arbitrary[String](Gen.chooseNum(Int.MinValue, Int.MaxValue).map(_.toString))

  def map = prop { example: String =>
    column[String, Int]((s: String) => s.toInt).apply(example, meta).toEither must beRight(
      example.toInt
    )
  }.setArbitrary(arbValidIntString)
}

```



