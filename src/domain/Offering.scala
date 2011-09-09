package domain

import scala.actors.Actor
import java.sql.Date

//messages:
trait OfferingMsg
case class IsYourCourse(course: Course, target: Actor) extends OfferingMsg




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
        case IsYourCourse(crs, target) =>
          debug( this + "received " + IsYourCourse(crs, target))
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