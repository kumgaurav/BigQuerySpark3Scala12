package com.kumargaurav

import scala.collection._
import org.scalatest.Assertions
import org.junit.Test

class StackSuite extends Assertions {

  @Test def stackShouldPopValuesIinLastInFirstOutOrder() {
    val stack = new mutable.ArrayStack[Int]
    stack.push(1)
    stack.push(2)
    assert(stack.pop() === 2)
    assert(stack.pop() === 1)
  }

  @Test def stackShouldThrowRuntimeExceptionIfAnEmptyArrayStackIsPopped() {
    val emptyStack = new mutable.ArrayStack[String]
    intercept[RuntimeException] {
      emptyStack.pop()
    }
  }
}

/*
Here's an example of a FunSuite with Matchers mixed in:
*/
import org.scalatest.FunSuite
import org.scalatest.Matchers

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
@RunWith(classOf[JUnitRunner])
class ListSuite extends FunSuite with Matchers {

  test("An empty list should be empty") {
    List() should be ('empty)
    Nil should be ('empty)
  }

  test("A non-empty list should not be empty") {
    List(1, 2, 3) should not be ('empty)
    List("fee", "fie", "foe", "fum") should not be ('empty)
  }

  test("A list's length should equal the number of elements it contains") {
    List() should have length (0)
    List(1, 2) should have length (2)
    List("fee", "fie", "foe", "fum") should have length (4)
  }
}

/*
ScalaTest also supports the behavior-driven development style, in which you
combine tests with text that specifies the behavior being tested. Here's
an example whose text output when run looks like:

A Map
- should only contain keys and values that were added to it
- should report its size as the number of key/value pairs it contains
*/
import org.scalatest.FunSpec

class ExampleSpec extends FunSpec {

  describe("An ArrayStack") {

    it("should pop values in last-in-first-out order") {
      val stack = new mutable.ArrayStack[Int]
      stack.push(1)
      stack.push(2)
      assert(stack.pop() === 2)
      assert(stack.pop() === 1)
    }

    it("should throw RuntimeException if an empty array stack is popped") {
      val emptyStack = new mutable.ArrayStack[Int]
      intercept[RuntimeException] {
        emptyStack.pop()
      }
    }
  }
}
