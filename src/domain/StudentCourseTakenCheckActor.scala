package domain

import scala.actors.Actor;
import scala.actors.Actor._;

class StudentCourseTakenCheckActor(
  var student: Student, val course: Course, numOfExpectedResponses: Int, val target: Actor  ) extends BaseDomain {

  private var numOfResponses: Int = 0

  override def act() {
    loop {
      react {

//        case HasTaken(course, target_) =>
//          /* */debug(this + " received message: " + HasTaken(course, target_ ))
//          target = target_
//          hasTaken(course);

        case AreYouCurrentTermCourseResponse(course, true) =>

          numOfResponses += 1;
          /* */debug(this + " received " + AreYouCurrentTermCourseResponse(course, true))
          sendResponse(course, true)

        case AreYouCurrentTermCourseResponse(course, false) =>

          numOfResponses += 1;
          /* */debug(this + " received " + AreYouCurrentTermCourseResponse(course, false))
          if (numOfResponses == numOfExpectedResponses) {
            /* */debug(this + " received " + numOfResponses + " responses from studyRecords and none was a taken_course record so we send Passed(false) ")
            sendResponse(course, false)
          }
      }
    }
  }

//  def hasTaken(course: Course) = {
//	  for (sr <- student.studyRecords)
//		  sr ! AreYouCurrentTermCourseRequest(course, self)
//  }

  def sendResponse(course: Course, result: Boolean) {
    target ! Taken(course, result)
    exit
  }

  override def toString = "[StudentCourseTakenCheckActor of " + student + "]"

}