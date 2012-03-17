package domain

import scala.actors.Actor
import java.sql.Date

//messages:




class Offering(
  var id: String,
  var course: Course,
  var section: Int,
  var examDate: Date,
  var term: Term) extends BaseDomainClass {

  override def act() {
    loop {
      react {
        case SayId =>
          println(id)
        case IsYourCourseRequest(crs, target) =>
          debug( this + "received " + IsYourCourseRequest(crs, target))
          if (crs.equals(this.course)) {
            debug( this + "replied true")
            sender ! IsYourCourseResponse(crs, true, target)
          } else {
            debug(this + "replied false")
            sender ! IsYourCourseResponse(crs, false, target)
          }
          
        case exit =>
          exit
      }
    }
  }
  override def toString(): String = {
    return "[Offering: id=" + id + " course=" + course + "]"
  }
}