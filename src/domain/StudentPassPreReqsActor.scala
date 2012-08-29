package domain

import scala.actors.Actor
import scala.actors.Actor._;

class StudentPassPreReqsActor(
  var student: Student, var requirement: Requirement, var target: Actor) extends BaseDomain {

  private var numOfResponses: Int = 0

  override def act() {
    if (requirement.prerequisites.size == 0) {
      /* */ debug(requirement + " has no prerequisites so we send " + PassedPres(requirement, true))
      target ! PassedPres(requirement, true)
      exit;
    } else {
      hasPassedPreReqs(requirement);

      loop {
        react {
          case Passed(pre, false) =>
            target ! PassedPres(requirement, false)
            /* */ debug(this + " received " + Passed(pre, false) + " so we send " + PassedPres(requirement, false) + " to target")
            exit;
          case Passed(pre, true) =>
            numOfResponses += 1;
            /* */ debug(this + " received " + Passed(pre, true))
            if (numOfResponses == requirement.prerequisites.size) {
              /* */ debug(this + " received " + numOfResponses + " 'pass' responses from pre requisite courses so we send PassedPres(true) ")
              target ! PassedPres(requirement, true)
              exit;
            }
        }
      }
    }

  }

  def hasPassedPreReqs(req: Requirement) = {
    for (pre <- req.prerequisites)
      student ! HasPassed(pre.course, this)
  }

  override def toString = "[StudentPassPreReqsActor of " + student + "]"

}