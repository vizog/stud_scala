package domain

import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.OutputChannel
import repository.StudentRepository

case object RetakeCourseAllowedCondition extends ConditionMessage
case object TakeWithoutPreReqsAllowedCondition extends ConditionMessage

class StudentTakeCourseActor(

  var student: Student) extends BaseDomain {
  def this() = this(null)

  private var offering: Offering = null
  private var target: Actor = null
  private var numOfResponses: Int = 0
  private var start_time:Long = 0
  
  override def act() {
    loop {
      react {
        case TakeCourse(offering_, target_) =>
          offering = offering_
          target = target_
          debug(this + " received " + TakeCourse(offering, target_))

//          start_time = System.currentTimeMillis()
          //validate : 
          //check that student has not taken more than 20 units
          student !  NumOfTermTakenUnitsAssertionRequest(this, offering.course)

          //check that student has not already passed the course:
//          offering.term.termRegulation ! ConditionalContinuation(RetakeCourseAllowedCondition, Passed(offering.course, false), this, student, HasPassed(offering.course, this))
          
          //check that student has passed prerequisites
//          student.program !  HasPassedPreReqs(student, offering.course, this);
          
          //check that student has not already taken this course
//          student ! HasTaken(offering.course, this)
          
           //###
        case Passed(course, true) =>
          sendResponse(false, "student " + student + " has already passed the course: " + course)

        case Passed(course, false) =>
          debug("validate -> already passsed? -> OK.  Stepping forward")
          stepForward()

        case Taken(course, true) =>
          sendResponse(false, "student " + student + " has already taken the course: " + course)

        case Taken(course, false) =>
          debug("validate -> already taken? -> OK.  Stepping forward")
          stepForward()

        case PassedPres(course, false) =>
          debug("validate -> passed pres? -> FAIL")
          sendResponse(false, "student " + student + " has not passed all prerequisites for the course: " + course)

        case PassedPres(course, true) =>
          debug("validate -> passed pres? -> OK. Stepping forward")
          stepForward()

        case NumOfTermTakenUnitsAssertionResult(result, takenUnits) =>
          if (! result)
            sendResponse(false, "student " + student + " can't take more than allowed number of units. already taken units = " + takenUnits)
          else
            stepForward()
      }
    }
  }
  def stepForward() {
    numOfResponses += 1
    if (numOfResponses == 4) {
      //4 validation steps 
      //take the course
      val sr: StudyRecord = new StudyRecord(-1, offering)
      StudentRepository.saveStudyRecord(student, sr)
      debug("student " + student + " successfully took course: " + offering.course )
      sendResponse(true, "all checks have passed")

    }
  }

  def sendResponse(result: Boolean, comment: String) {
    target ! TakeCourseResponse(result, comment)
//    println("sent "  + TakeCourseResponse(result, comment) + " to " + target)
    exit
  }

  override def toString = "[StudentTakeCourseActor of " + student + "]"

}