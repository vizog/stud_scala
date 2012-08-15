package domain

import scala.actors.Actor
import scala.actors.Actor._;
import repository.TermRepository

class StudentNumOfUnitsValidatorActor(var student: Student, var course:Course ) extends BaseDomain {
  private var target: Actor = null
  private var numOfResponses: Int = 0
  private var numOfSteps: Int = 0
  private var totalUnits: Int = 0
  override def act() {
    loop {(
      react ({
        case NumOfTermTakenUnitsAssertionRequest(target_) =>
          target = target_
          numOfSteps = student.studyRecords.size
          /* */debug(StudentNumOfUnitsValidatorActor.this + " received message: " + NumOfTermTakenUnitsAssertionRequest(target_))
          for (rec <- student.studyRecords) yield rec ! NumOfTermTakenUnitsAssertionRequest(StudentNumOfUnitsValidatorActor.this)
        case NumOfTermTakenUnits(units) =>
          totalUnits += units
          stepForward()
      }))
    }
  }
  def stepForward() {
    numOfResponses += 1
    if (numOfResponses == numOfSteps) {
      val currentTerm = TermRepository.getCurrentTerm()
      //using conditional continuation pattern:
      //termRegulation checks the violation of maximum allowed units rule. if this asserts (i.e num of units <= max allowed units) then it sends the
      //NumOfTermTakenUnitsAssertionResult(true) to the target (which is StudentTakeCourseActor) else it sends NumOfTermTakenUnitsAssertionResult(false) to the target
      currentTerm.termRegulation ! ConditionalContinuation(MaxAllowedUnitsCondition(totalUnits + course.units), NumOfTermTakenUnitsAssertionResult(true, totalUnits), target, target: Actor, NumOfTermTakenUnitsAssertionResult(false, totalUnits))
    }
  }
}
case class MaxAllowedUnitsCondition(takenUnits: Int) extends ConditionMessage
