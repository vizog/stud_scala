package domain

import scala.actors.Actor
import scala.actors.Actor._;
import scala.actors.OutputChannel

case class TakeCourseResponse(result: Boolean, comment: String)

class StudentTakeCourseActor(

  var student: Student) extends BaseDomainClass {
  def this() = this(null)

  private var course: Course = null
  private var target: Actor  = null
  private var numOfResponses: Int = 0

  override def act() {
    loop {
      react {
        case TakeCourse(course_,target_) =>
          course = course_
          target = target_
          debug(this + " received " + TakeCourse(course_,target_))
          //validate : 
          //check that he/she has not already passed the course:
          student ! HasPassed3(course, this)

          //check that student has passed all prerequisites:
          val coursePassPres = new StudentPassPreReqsActor(student)
          coursePassPres.start
          coursePassPres ! HasPassedPreReqs(course, this)

        case Passed(course, true) =>
          sendResponse(false, "student " + student + " has already passed the course: " + course)


        case Passed(course, false) =>
          debug("validate -> already passsed? -> OK.  Stepping forward")
          stepForward()

        case PassedPres(course, false) =>
          debug("validate -> passed pres? -> FAIL")
          sendResponse(false, "student " + student + " has not passed all prerequisites for the course: " + course)

        case PassedPres(course, true) =>
          debug("validate -> passed pres? -> OK. Stepping forward")
          stepForward()
      }
    }
    def stepForward() {
      numOfResponses += 1
      if(numOfResponses == 2)//2 validation steps (already passed and passed prerequisites)
        sendResponse(true,"all checks have passed")
    }
  }
  
  def sendResponse(result:Boolean, comment:String) {
	  target ! TakeCourseResponse(result,comment)
	  exit
  } 

  override def toString = "[StudentTakeCourseActor of " + student + "]"

}