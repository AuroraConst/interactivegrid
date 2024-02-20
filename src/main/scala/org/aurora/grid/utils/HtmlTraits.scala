package org.aurora.grid.utils

// import org.aurora.grid.impl.calendar.{Grid, GridData, Header}
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

trait HtmlAble[T]:
  extension (x:T) 
    def htmlElement:HtmlElement


