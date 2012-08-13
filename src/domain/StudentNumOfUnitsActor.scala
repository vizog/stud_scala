//#ADDED
package domain

import scala.actors.Actor
import scala.actors.Actor._;

class StudentNumOfUnitsActor(var student: Student) extends BaseDomain {
  private var target: Actor = null
  private var numOfResponses: Int = 0
  private var numOfSteps: Int = 0
  private var totalUnits: Int = 0
  override def act() {
    loop {
      react {
        case NumOfCurrentTermTakenUnitsRequest(target_) =>
          target = target_
          numOfSteps = student.studyRecords.size
          //debug(this + " received message: " + NumOfTakenUnitsRequest(term, target_))
          for (rec <- student.studyRecords)
            rec ! NumOfCurrentTermTakenUnitsRequest(this)
        case NumOfCurrentTermTakenUnitsResponse(units) =>
          totalUnits += units
          stepForward()
      }
    }
  }
   def stepForward() {
    numOfResponses += 1
    if (numOfResponses == numOfSteps) {
     target ! NumOfCurrentTermTakenUnitsResponse(totalUnits)
     //debug(this + " sent message: " + NumOfTakenUnitsResponse(totalUnits))
    }
  }
}
//###