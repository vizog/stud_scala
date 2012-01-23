package domain

import scala.actors.Actor;
import scala.actors.Actor._;

//messages:

trait StudyRecordMessage
trait StudyRecordMessageReply

case object SayGrade extends StudyRecordMessage
case class AreYouAPassCourseRecord(course: Course, target: Actor) extends StudyRecordMessage

class StudyRecord(
  var grade: Double,
  var offering: Offering) extends BaseDomainClass {

  def act() {
    loop {
      react {
        case SayGrade =>
          println(grade)

        case AreYouAPassCourseRecord(course, target) =>
          debug("[StudyRec:" + this + "] received: AreYouAPassCourseRecord("+course+", "+target+")");
          if (grade < 10) {
            debug("[StudyRec:" + this + "] sent: Passed(" + course + ", false)");
            reply(Passed(course, false));
          }
          else {
            val res = offering !? IsYourCourse(course, self)
            debug("[StudyRec:" + this + "] sent: Passed(" + course + ", " + res + ")");
            reply(Passed(course, res.asInstanceOf[Boolean]))
          }
        case exit =>
          exit
      }
    }
  }
  
  override def toString():String = {
    return offering.id + "->" + grade 
  }
}