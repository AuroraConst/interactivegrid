package org.aurora.grid.utils

import org.scalatest.*
import wordspec.*
import matchers.*
import com.raquo.airstream.state.Var
import scala.collection.mutable.ListBuffer

class LLBufferTest extends AnyWordSpec with should.Matchers:
  "LBufferTest (One dimensional buffer)" should {
    "allocate according to dim (size >0)" in {
      val lbufferdim = new LLBufferDim[Coordinate]:
        def defaultData(col:Int,row:Int) = Coordinate(col,row)

      val buf = lbufferdim.dimRow(5,0)
      buf.size should be(5)
      buf should be(ListBuffer(Coordinate(0,0),Coordinate(1,0),Coordinate(2,0),Coordinate(3,0),Coordinate(4,0)))
    }

    "allocate according to dim (0)" in {
      val lbufferdim = new LLBufferDim[Int]:
        def defaultData(row:Int,col:Int): Int = -1

      val buf = lbufferdim.dimRow(0,0)
      buf.size should be(0)
      buf should be(ListBuffer())

      // buf should be (lbufferdim.dimRow(0)) //default row index is set to 0
    }
 
  }

  "LLBufferTest (2 dimensional buffer) with Option" should {
    "work" in {
      val llbufferdim = new LLBufferDim[Option[Coordinate]]: //anonymous class from trait
        def defaultData(row:Int,col:Int) = Some(Coordinate(col,row))

      val buf = llbufferdim.dim(4,5)  //(rowsize,columnsize)
      buf(0).size should be(4) //row size should be 4
      info(s"${buf(0)}")
      info(s"${buf(1)}")
      info(s"${buf(2)}")
      info(s"${buf(3)}")
      info(s"${buf(4)}")

      buf.size should be(5)  //column size should be 5
    }
  }
