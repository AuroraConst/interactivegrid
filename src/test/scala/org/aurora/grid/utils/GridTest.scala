package org.aurora.grid.utils

import org.scalatest.*
import wordspec.*
import matchers.*
import com.raquo.airstream.state.Var
import scala.collection.mutable.ListBuffer
import scala.collection.mutable

class GridTest extends AnyWordSpec with should.Matchers:
  "extending GridDataT to use primitive Int" should {
    "work like this " in {

      case class GridDataImpl(_data: Int) extends GridDataT[Int]:
        override val data = Var[Int](_data)

      case class Grid(col: Int, row: Int) extends GridT[Int](col, row):
        override lazy val grid = dim(col, row)
        override def defaultData(col:Int,row:Int) = GridDataImpl(0)
    }
  }

  "extending GridDataT to use Option[Int]" should {
    case class GridDataImpl(_data: Option[Int]) extends GridDataT[Option[Int]]:
      override val data = Var[Option[Int]](_data)
    end GridDataImpl

    case class Grid(col: Int, row: Int) extends GridT[Option[Int]](col, row):
      override lazy val grid = dim(col, row)
      override def defaultData(col:Int,row:Int) = GridDataImpl(None)
    end Grid
    "work like this" in {
      Grid(3,5).grid.size shouldBe 5
      Grid(3,5).grid(0).size shouldBe 3


      Grid(3,5).gridData(4,5) should be(Left(BoundsError.NotInBounds(4,5)))
      Grid(3,5).gridData(4,5).toOption should be(None)
      Grid(3,5).gridData(0,0) should be (Right(GridDataImpl(None)))
      Grid(3,5).gridData(0,0).toOption should be (Some(GridDataImpl(None)))
      Grid(3,5).gridData(0,0).toOption.map{ _.data.now()} match {
        case Some(None) => succeed
        case _ => fail()
      }

    }
  }
