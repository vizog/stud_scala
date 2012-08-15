package domain

import scala.actors.Actor;
import scala.actors.Actor._;

class StudentCoursePassActor(
  var student: Student) extends BaseDomain {
  def this() = this(null)

  private var target: Actor = null
  private var numOfResponses: Int = 0

  override def act() {
    loop {
      react {

        case HasPassed(course, target_) =>

          /* */debug(this + " received message: " + HasPassed(course, target_ ))
          target = target_
          hasPassed(course);

        case AreYouAPassCourseResponse(course, true) =>

          numOfResponses += 1;
          /* */debug(this + " received " + AreYouAPassCourseResponse(course, true))
          sendResponse(course, true)

        case AreYouAPassCourseResponse(course, false) =>

          numOfResponses += 1;
          /* */debug(this + " received " + AreYouAPassCourseResponse(course, false))
          if (numOfResponses == student.studyRecords.size) {
            /* */debug(this + " received " + numOfResponses + " responses from studyRecords and none was a pass record so we send Passed(false) ")
            sendResponse(course, false)
          }
      }
    }
  }

  def hasPassed(course: Course) = {
    for (sr <- student.studyRecords)
      sr ! AreYouAPassCourseRequest(course, self)
  }
  
  def sendResponse(course: Course, result: Boolean) {
    target ! Passed(course, result)
    exit
  }

  override def toString = "[StudentCoursePassActor of " + student + "]"

}