package org.aurora.grid.utils

import collection.mutable.ListBuffer
import com.raquo.laminar.api.L.{*, given}



trait GridDataT[D] :
  val data:Var[D]

trait GridT[D](cols:Int,rows:Int) extends LLBufferDim[GridDataT[D]]:
  lazy val grid:ListBuffer[ListBuffer[GridDataT[D]]] = dim(cols,rows)

  val xRange = (0 until cols)
  val yRange = (0 until rows)
 

  def inBounds(c:Coordinate): Boolean = 
    xRange.contains(c.column) && yRange.contains(c.row)

  def update(c:Coordinate, data:D):Unit =
    if(inBounds(c))   grid(c.row)(c.column).data.set(data) 

  def gridData(c:Coordinate):Either[BoundsError,GridDataT[D]] = 
    if(inBounds(c))
      Right(grid(c.row)(c.column))   else   Left(BoundsError.NotInBounds(c.column,c.row))

  def gridData(col:Int,row:Int):Either[BoundsError,GridDataT[D]] = 
        gridData(Coordinate(col,row))  
 
end GridT



import collection.mutable.ListBuffer  
/**
  * allocates 2 dimensional ListBuffers[D]  as specified by [T]
  */
trait LLBufferDim[D] :
  def emptyRow:ListBuffer[D] = ListBuffer[D]()
  lazy val emptyBuffer:ListBuffer[ListBuffer[D]] = ListBuffer()
  def defaultData(col:Int,row:Int):D

  def dimRow(rowSize:Int,_rowIndex:Int):ListBuffer[D] =
    (0 until rowSize)
      .foldLeft(emptyRow){
        (lb,counter) => lb.addOne(defaultData(counter,_rowIndex))
      } 

  def dim(_rowSize:Int,_colSize:Int) :ListBuffer[ListBuffer[D]] =

    (0 until _colSize)
      .foldLeft(emptyBuffer){
        (lb,rowcounter) => {
          lb.addOne(  dimRow(_rowSize,rowcounter))
        }
    }

