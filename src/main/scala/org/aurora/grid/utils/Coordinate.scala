package org.aurora.grid.utils


enum BoundsError:
  case NotInBounds(col:Int,row:Int)
  
case class Coordinate(column:Int,row:Int) :
  def addX(x:Int):Coordinate =
    this.copy(column = this.column+x)
  def addY(y:Int):Coordinate =
    this.copy(row= this.row+ y)
