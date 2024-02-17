package org.aurora
import scala.scalajs.js
import scala.scalajs.js.annotation.*

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

import org.aurora.grid.utils.*
import scala.collection.mutable.ListBuffer

case class GridDataImpl(grid: Grid, coordinate: Coordinate, _data: Option[Int])
    extends GridDataT[Option[Int]]:

  lazy val htmlElement: HtmlElement = divCell(grid, this)
  val tabIndex: Var[Int] = Var(0)
  val toggleState: Var[EditorToggleState] = Var(EditorToggleState.UnSelected)
  override val data = Var[Option[Int]](_data)

end GridDataImpl

case class Grid(colSize: Int, rowSize: Int)
    extends GridT[Option[Int]](colSize, rowSize):

  val focusedCoodinate = Var[Option[Coordinate]](Some(Coordinate(0, 0)))
  override def defaultData(col: Int, row: Int) =
    GridDataImpl(this, Coordinate(col, row), Some(0))

  def selectedRow(row: Int): ListBuffer[GridDataImpl] =
    grid(row).asInstanceOf[ListBuffer[GridDataImpl]]

end Grid

given HtmlAble[ListBuffer[GridDataImpl]] with
  extension (lb: ListBuffer[GridDataImpl])
    def htmlElement: HtmlElement =
      tr(
        lb.map(gridData => gridData.htmlElement).toArray*
      )

given HtmlAble[Grid] with
  extension (g: Grid)
    def htmlElement: HtmlElement =
      def row(y: Int) = g.xRange.map(x =>
        td(g.gridData(x, y).toOption.get.asInstanceOf[GridDataImpl].htmlElement)
      )
      def rows = g.yRange.map(y => tr(row(y))).toArray

      table(
        // thead(headers*),
        tbody(rows*)
      )

enum EditorToggleState(colorString: String):
  case UnSelected extends EditorToggleState("green")
  case StateFocused extends EditorToggleState("red")
  case RowSelected extends EditorToggleState("#9F5EB2")
  //Note: you have to surface properties of the enum to the outside
  lazy val color = colorString

import EditorToggleState.*

def divCell(grid: Grid, gd: GridDataImpl): HtmlElement =
  div(
    child.text <-- gd.data.signal.map(data =>
      s"${gd.coordinate.column},${gd.coordinate.row}"
    ),
    tabIndex <-- gd.tabIndex.signal,
    backgroundColor <-- gd.toggleState.signal.map(_.color),
    typ := "text",
    // onInput.mapToValue --> gd.varDataWriter,
    // onInput.mapToValue.map(x => gd.varData.now().toString()) --> gd.g.summaryText,
    onKeyDown --> (e =>
      def htmlInputFocus(c: Coordinate) =
        grid.gridData(c).map(_.asInstanceOf[GridDataImpl]).foreach {
          _.htmlElement.ref.focus()
        }
      e.keyCode match
        case 40 => //down cursor
          htmlInputFocus(gd.coordinate.addY(1))
        case 38 => //up cursor
          htmlInputFocus(gd.coordinate.addY(-1))
        case 37 => //left cursor
          htmlInputFocus(gd.coordinate.addX(-1))
        case 39 => //right cursor
          htmlInputFocus(gd.coordinate.addX(+1))
        case 9 => //tab
    //   dom.window.console.log(s"tabbed ${gd.coordinate}tab tab tab ")

    // case _ => ()
    ),
    onFocus --> (e =>
      grid.focusedCoodinate.update(_ => Some(gd.coordinate))
      grid
        .selectedRow(gd.coordinate.row)
        .foreach((d: GridDataImpl) => d.toggleState.update(_ => RowSelected))
      gd.toggleState.update(_ => StateFocused)
    // gd.g.headers(gd.coordinate.x).selected.update(_ => true)
    ),
    onBlur --> (e => //focus out
      gd.toggleState.update(_ => UnSelected)
      grid
        .selectedRow(gd.coordinate.row)
        .foreach(d => d.toggleState.update(_ => UnSelected))
    // gd.g.headers(gd.coordinate.x).selected.update(_ => false)
    )
  )

@main
def Main(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Grid(35, 450).htmlElement
  )

end Main
