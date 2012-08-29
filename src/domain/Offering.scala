package domain

import scala.actors.Actor
import java.sql.Date
import repository.OfferingRepository

//messages:

class Offering(
  var id: String,
  var course: Course,
  var section: Int,
  var examDate: Date,
  var term: Term, var locked: Boolean) extends BaseDomain {
def this(id: String, course: Course, section: Int, examDate: Date, term:Term) = this(id,course,section,examDate,term,false)
  override def act() {
    loop {
      react {
        case IsYourCourseRequest(crs, target) =>
          /* */debug(this + "received " + IsYourCourseRequest(crs, target))
          if (crs.equals(this.course)) {
            /* */debug(this + "replied true")
            sender ! IsYourCourseResponse(crs, true, target)
          } else {
            /* */debug(this + "replied false")
            sender ! IsYourCourseResponse(crs, false, target)
          }

        case CourseGradeRequest( term_, target, result) =>
          /* */debug(this + " received " + CourseGradeRequest( term_, target, result) )
          term.start

          result match {

            case CourseGradeResponse(false, grade, null, 0) =>
              // this comes from study record because value for isForTerm is false (note that in scala, Boolean is not nullable)
              // send to Term so it checks if the gpa request is for this offering's term.
              term ! CourseGradeRequest(term_, target, CourseGradeResponse(false, grade, null, 0))
              /* */debug(this + " sent " + CourseGradeRequest(term_, target, CourseGradeResponse(false, grade, null, 0)) + " to " + term)
                   
            case CourseGradeResponse(true, grade, null, 0) =>
              //this comes from Term, because value for isForTerm is true.
              // forward it to course to fill in the rest of parameters 
              course.start
              course ! CourseGradeRequest(term_, target, CourseGradeResponse(true, grade, null, 0))
              /* */debug(this + " sent " + CourseGradeRequest(term_, target, CourseGradeResponse(true, grade, null, 0)) + "to " + course)
            
          }
          
        case LockOffering =>
          locked = true
          OfferingRepository.saveOffering(this);
           reply("done")
      }
    }
  }
  override def toString(): String = {
    return "[Offering: id=" + id + " course=" + course + "]"
  }
}