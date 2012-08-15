package domain
import actors.Actor
import java.sql.Date;
import repository.OfferingRepository

class TermRegulation(var maxAllowedUnits: Int, var reTakeCourseAllowed: Boolean, var takeWithoutPassPresAllowed: Boolean) extends BaseDomain {

  override def act() {
    loop {
      react {
        //	      ConditionalContinuation(condition:String, assertionResponse:Any, assertionTarget: Actor, continuationTarget: Actor, continuationMessage: Any)
        case ConditionalContinuation(condition, assertionResponse, assertionTarget, continuationTarget, continuationMessage) =>
          /* */ debug(this + " received: " + ConditionalContinuation(condition, assertionResponse, assertionTarget, continuationTarget, continuationMessage))
          condition match {
            case MaxAllowedUnitsCondition(totalUnits) =>
              sendMessage((totalUnits <= maxAllowedUnits), assertionResponse, assertionTarget, continuationTarget, continuationMessage)
            case RetakeCourseAllowedCondition =>
              sendMessage(reTakeCourseAllowed, assertionResponse, assertionTarget, continuationTarget, continuationMessage)
            case TakeWithoutPreReqsAllowedCondition =>
              sendMessage(takeWithoutPassPresAllowed, assertionResponse, assertionTarget, continuationTarget, continuationMessage)
          }
      }
    }
  }

  def sendMessage(assertion: Boolean, assertionResponse: Any, assertionTarget: Actor, continuationTarget: Actor, continuationMessage: Any) = {
    if (assertion) {
    	assertionTarget ! assertionResponse
    	/* */ debug(this + " sent: " + assertionResponse + " to : " + assertionTarget)
    }
    else {
    	continuationTarget ! continuationMessage
    	/* */ debug(this + " sent: " + continuationMessage + " to : " + continuationTarget)
    }
  }

  override def toString() = {
    val retake = if (reTakeCourseAllowed) " retake allowed " else "" + "retake not allowed"
    val pres = if (takeWithoutPassPresAllowed) " no pres allowed " else "" + "no pres not allowed"
    "TermRegulation max allowed: " + maxAllowedUnits + retake + pres
  }
}