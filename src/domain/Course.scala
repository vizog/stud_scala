package domain
import actors.Actor
import java.sql.Date
import repository.CourseRepository
import util.LoggingSupport

case object Save

class Course(
  var id: String,
  var name: String,
  var units: Int,
  var preRequisites: List[Course]) extends BaseDomain {
  def this() = this(null, null, 0, Nil)

  override def act() {
    loop {
      react {
        case CourseGradeRequest(term: Term, target: Actor, CourseGradeResponse(true, grade, null, 0)) =>
          debug(this + " received " + CourseGradeRequest(term: Term, target: Actor, CourseGradeResponse(true, grade, null, 0)) )
          //append course name and units to the result and send it to coordinator
          target ! CourseGradeResponse(true, grade, name, units)
          debug(this + " sent " + CourseGradeResponse(true, grade, name, units) + " to " + target) 
      }
    }

  }

  override def toString = "[course: id= " + id + ", name " + name + ", units = " + units + "]"
  override def equals(other: Any): Boolean =
    other match {
      case that: Course =>
        return (that canEqual this) &&
          id == that.id

      case _ => false
    }

  def canEqual(other: Any): Boolean =
    other.isInstanceOf[Course]

}