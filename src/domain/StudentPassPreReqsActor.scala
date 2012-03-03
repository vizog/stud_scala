package domain

import scala.actors.Actor
import scala.actors.Actor._;

class StudentPassPreReqsActor(
  var student: Student) extends BaseDomainClass {
  def this() = this(null)

  private var target: Actor = null
  private var numOfResponses: Int = 0
  private var course: Course = null

  override def act() {
    loop {
      react {

        case HasPassedPreReqs(course_, target_) =>
          course = course_
          target = target_

          debug(this + " received message: " + HasPassedPreReqs(course, target_))
          if(course.preRequisites.size == 0) {
            debug(course + " has no prerequisites so we send " + PassedPres(course, true))
            target ! PassedPres(course, true)
          } else {
        	  hasPassedPreReqs(course);
          }

        case Passed(pre, false) =>
          target ! PassedPres(course, false)
          debug(this + " received " + Passed(pre, false) + " so we send " + PassedPres(course, false) + " to target")

        case Passed(pre, true) =>
          numOfResponses += 1;
          debug(this + " received " + Passed(pre, true))

          if (numOfResponses == course.preRequisites.size) {
            debug(this + " received " + numOfResponses + " 'pass' responses from pre requisite courses so we send PassedPres(true) ")
            target ! PassedPres(course, false)
          }

        case a: Any =>
          debug(this + " received " + a)
      }
    }
  }

  def hasPassedPreReqs(course: Course) = {
    for (pre <- course.preRequisites)
      student ! HasPassed3(pre, this)
  }

  override def toString = "[StudentPassPreReqsActor of " + student + "]"

}