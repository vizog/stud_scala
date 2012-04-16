package domain

import scala.actors.Actor
import java.sql.Date

class Offering(
  var id: String,
  var course: Course,
  var section: Int,
  var examDate: Date,
  var term: Term) extends BaseDomainClass {

  override def act() {
    loop {
      react {
        case IsYourCourseRequest(crs, target) =>
          debug( this + " received " + IsYourCourseRequest(crs, target))
          if (crs.equals(this.course)) {
            debug( this + " replied true")
            reply(true);
          } else {
            debug(this + " replied false")
            reply(false);

          }
          
        case HasPassed(crs, target) =>
        debug( this + " received " + HasPassed(crs, target))
        if (crs.equals(this.course)) {
        	debug( this + " replied true")
        	target ! Passed(crs,true);
        } else {
        	debug(this + " replied false")
        	target ! Passed(crs,false);
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