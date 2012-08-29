package domain

import scala.actors.Actor
import scala.actors.Actor._;
import repository.TermRepository

class StudentNumOfUnitsValidatorActor(var student: Student, var course:Course, val numOfSteps: Int, target:Actor ) extends BaseDomain {
  private var numOfResponses: Int = 0
  private var totalUnits: Int = 0
  override def act() {
    loop {(
      react ({
//        case NumOfTermTakenUnitsAssertionRequest(target_) =>
//          target = target_
//          numOfSteps = student.studyRecords.size
//          /* */debug(StudentNumOfUnitsValidatorActor.this + " received message: " + NumOfTermTakenUnitsAssertionRequest(target_))
//         student ! NumOfTermTakenUnitsAssertionRequest(StudentNumOfUnitsValidatorActor.this)
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
      currentTerm.termRegulation ! ConditionalContinuation(MaxAllowedUnitsCondition(totalUnits + course.units), NumOfTermTakenUnitsAssertionResult(true, totalUnits), target, target, NumOfTermTakenUnitsAssertionResult(false, totalUnits))
    }
  }
}
case class MaxAllowedUnitsCondition(takenUnits: Int) extends ConditionMessage
