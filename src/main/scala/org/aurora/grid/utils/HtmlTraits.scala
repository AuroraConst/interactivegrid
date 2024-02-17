package org.aurora.grid.utils

// import org.aurora.grid.impl.calendar.{Grid, GridData, Header}
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

trait HtmlAble[T]:
  extension (x:T) 
    def htmlElement:HtmlElement


// given HtmlAble[Header] with 
//   extension(h:Header)
//     def htmlElement: HtmlElement = th(h.header,
//       backgroundColor <-- h.selected.signal.map( _ match
//           case true => "orange"
//           case false => "purple"
//       )
//     )

// given HtmlAble[Grid] with
//   extension(g: Grid) 
//     def htmlElement: HtmlElement = 
//       def row(y:Int) = g.xRange.map(x => td(g.data(x,y).map( x => x.inputHtmlElement  ).getOrElse(div("error")))) 
//       def rows = g.yRange.map(y => tr(row(y)))
//       def headers = g.headers.map{h => h.htmlElement }

//       table(
//           thead(headers*),
//           tbody(rows*)
//       )
