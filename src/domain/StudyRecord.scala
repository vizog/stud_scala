package domain

import scala.actors.Actor;
import scala.actors.Actor._;

//messages:

trait StudyRecordMessage
trait StudyRecordMessageReply

case object SayGrade extends StudyRecordMessage
case class AreYouAPassCourseRequest(course: Course, target: Actor) extends StudyRecordMessage
case class AreYouAPassCourseResponse(course: Course, result:Boolean) extends StudyRecordMessage

class StudyRecord(
  var grade: Double,
  var offering: Offering) extends BaseDomainClass {

  def act() {
    loop {
      react {
        case AreYouAPassCourseRequest(course, target) =>
          
          debug("[StudyRec:" + this + "] received: AreYouAPassCourseRecord(" + course + ", " + target + ")");
          if (grade < 10) {
            debug("[StudyRec:" + this + "] sent: Passed(" + course + ", false) to " + target);
            sender ! AreYouAPassCourseResponse(course, false);
          } else {
        	offering ! IsYourCourseRequest(course, target)
            debug("[StudyRec:" + this + "] sent: HasPassed2(" + course + ", " + self + ") + to " + offering);
          }
          
        case IsYourCourseResponse(course, result, target) => //this come from Offering. the response should be sent to StudentCoursePassActor
          target ! AreYouAPassCourseResponse(course, result)
          println
        case exit =>
          exit
      }
    }
  }

  override def toString(): String = {
    return offering.id + "->" + grade
  }
}