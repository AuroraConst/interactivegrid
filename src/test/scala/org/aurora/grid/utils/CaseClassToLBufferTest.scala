package org.aurora.grid.utils

import org.scalatest.*
import wordspec.*
import matchers.*
import com.raquo.airstream.state.Var
import scala.collection.mutable.ListBuffer
import scala.collection.mutable

class CaseClassToLBufferTest extends AnyWordSpec with should.Matchers:
  "extending GridDataT to use primitive Int" should {
    case class Person(name: String, age: Int)

    case class GridDataImpl(c:Coordinate, _data: Option[Person]) extends GridDataT[Option[Person]]:
      override val data = Var[Option[Person]](_data)
    end GridDataImpl

    val grid = new GridT[Option[Person]](2, 4) {
			override def defaultData(col:Int,row:Int) = GridDataImpl(Coordinate(col,row), None) 
		}
    val persons = List(Person("arnold", 58), Person("connor", 25))

    "work like this " in {

      val columnFieldMap:Map[Int,Person => String] = Map(
				0 -> ((p: Person) => p.name) ,
				1 -> ((p:Person) => p.age.toString)
			)

      // def f (p:Person, index:Int):ListBuffer[String] =
    }
  }

  "extending GridDataT to use Option[Int]" should {
    case class GridDataImpl(_data: Option[Int]) extends GridDataT[Option[Int]]:
      override val data = Var[Option[Int]](_data)
    end GridDataImpl

    case class Grid(col: Int, row: Int) extends GridT[Option[Int]](col, row):
      override lazy val grid = dim(col, row)
      override def defaultData(col: Int, row: Int) = GridDataImpl(None)
    end Grid
    "work like this" in {
      Grid(3, 5).grid.size shouldBe 5
      Grid(3, 5).grid(0).size shouldBe 3

      Grid(3, 5).gridData(4, 5) should be(Left(BoundsError.NotInBounds(4, 5)))
      Grid(3, 5).gridData(4, 5).toOption should be(None)
      Grid(3, 5).gridData(0, 0) should be(Right(GridDataImpl(None)))
      Grid(3, 5).gridData(0, 0).toOption should be(Some(GridDataImpl(None)))
      Grid(3, 5).gridData(0, 0).toOption.map(_.data.now()) match
        case Some(None) => succeed
        case _          => fail()

    }
  }
