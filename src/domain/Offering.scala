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
        case IsYourCourseRequest(crs, target) =>
          debug( this + "received " + IsYourCourseRequest(crs, target))
          if (crs.equals(this.course)) {
            debug( this + "replied true")
            reply(true);
          } else {
            debug(this + "replied false")
            reply(false);

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