package org.aurora.grid.utils

import org.scalatest.*
import wordspec.*
import matchers.*
import com.raquo.laminar.api.L.{*, given}
import com.raquo.airstream.state.Var
import scala.collection.mutable.ListBuffer
import scala.collection.mutable

class GridHtmlableTest extends AnyWordSpec with should.Matchers:

  "extending GridDataT to use Option[Int] and extending from Htmable" should {
    case class GridDataImpl(_data: Option[Int]) extends GridDataT[Option[Int]] :
      override val data = Var[Option[Int]](_data)
    end GridDataImpl 


    given HtmlAble[GridDataImpl] with
      extension(g:GridDataImpl)
        override def htmlElement: HtmlElement = div("data: ", g.data.now().toString)



    case class Grid(col: Int, row: Int) extends GridT[Option[Int]](col, row):
      override lazy val grid = dim(col, row)
      override def defaultData(col:Int,row:Int) = GridDataImpl(Some(0))
    end Grid
    "work like this" in {
      val result = for{
        x <- Grid(1,1).gridData(0,0).toOption
      } yield(x.asInstanceOf[GridDataImpl])

    }
  }
