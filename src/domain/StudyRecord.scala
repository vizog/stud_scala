package domain

import scala.actors.Actor;
import scala.actors.Actor._;

//messages:

class StudyRecord(
  var grade: Double,
  var offering: Offering) extends BaseDomain {

  //if grade == -1 it means that the student has taken the offering in current term and hasn't passed or failed it yet
  def act() {
    loop {
      react {
        case AreYouAPassCourseRequest(course, target) =>
          /* */ debug("[StudyRec:" + this + "] received: " + AreYouAPassCourseRequest(course, target));
          if (grade < 10) {
            /* */ debug("[StudyRec:" + this + "] sent:" + AreYouAPassCourseResponse(course, false) + " to: " + target);
            target ! AreYouAPassCourseResponse(course, false);
          } else {
            actor {

              offering ! IsYourCourseRequest(course, target)
              /* */ debug("[StudyRec:" + this + "] sent: " + IsYourCourseRequest(course, target) + " to: " + offering);
              self.react {
                case IsYourCourseResponse(course, result, target) => //this comes from Offering. the response should be sent to StudentCoursePassActor
                  target ! AreYouAPassCourseResponse(course, result)
                  /* */ debug("[StudyRec:" + this + "] sent:" + AreYouAPassCourseResponse(course, result) + " to: " + target);
              }

            }
          }

        case AreYouCurrentTermCourseRequest(course, target) =>

          /* */ debug("[StudyRec:" + this + "] received: " + AreYouCurrentTermCourseRequest(course, target));
          if (grade != -1) {
            /* */ debug("[StudyRec:" + this + "] sent:" + Taken(course, false) + " to " + target);
            target ! AreYouCurrentTermCourseResponse(course, false);
          } else {

            actor {
              offering ! IsYourCourseRequest(course, target)
              self.react {
                case IsYourCourseResponse(course, result, target) =>
                  target ! AreYouCurrentTermCourseResponse(course, result)
                  /* */ debug("[StudyRec:" + this + "] sent: " + AreYouCurrentTermCourseResponse(course, result))
              }
            }
          }

        case CourseGradeRequest(term: Term, target, null) =>
          debug("[StudyRec:" + this + "] received:" + CourseGradeRequest(term: Term, target, null));
          //put the grade in result and send it along
          //note that first arguement of GPAItemResult (isForTerm) is false although we haven't checked it yet. 
          //this is because we can't set boolean to null.
          offering.start
          offering ! CourseGradeRequest(term, target, CourseGradeResponse(false, grade, null, 0))
          /* */ debug(this + " sent " + CourseGradeRequest(term, target, CourseGradeResponse(false, grade, null, 0)) + " to " + offering)
        //#ADDED
        case NumOfTermTakenUnitsAssertionRequest(target, course) =>
          //Ask Dr: is this valid?

          var units = offering.course.units
          if (grade != -1)
            units = 0; //because it is not for current term.
          target ! NumOfTermTakenUnits(units)
        //###
      }
    }
  }

  override def toString(): String = {
    return offering.id + "->" + grade
  }
}