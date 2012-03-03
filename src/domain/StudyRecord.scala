package domain

import scala.actors.Actor;
import scala.actors.Actor._;

//messages:

trait StudyRecordMessage
trait StudyRecordMessageReply

case object SayGrade extends StudyRecordMessage
case class AreYouAPassCourseRequest(course: Course, target: Actor) extends StudyRecordMessage
case class AreYouCurrentTermCourseRequest(course: Course, target: Actor) extends StudyRecordMessage
case class AreYouAPassCourseResponse(course: Course, result: Boolean) extends StudyRecordMessage
case class AreYouCurrentTermCourseResponse(course: Course, result: Boolean) extends StudyRecordMessage

class StudyRecord(
  var grade: Double,
  var offering: Offering) extends BaseDomainClass {

  //if grade == -1 it means that the student has taken the offering in current term and hasn't passed or failed it yet
  def act() {
    loop {
      react {
        case AreYouAPassCourseRequest(course, target) =>
          println("outer self:" + self)

          debug("[StudyRec:" + this + "] received: AreYouAPassCourseRecord(" + course + ", " + target + ")");
          if (grade < 10) {
            debug("[StudyRec:" + this + "] sent:" + AreYouAPassCourseResponse(course, false) + " to: " + target);
            sender ! AreYouAPassCourseResponse(course, false);
          } else {
            actor{
            	println("inner self:" + self)
            	offering ! IsYourCourseRequest(course, target)
            	debug("[StudyRec:" + this + "] sent: " +IsYourCourseRequest(course, target)  + " to: " + offering);
            	self.react {
            	   case IsYourCourseResponse(course, result, target) => //this come from Offering. the response should be sent to StudentCoursePassActor
            	   target ! AreYouAPassCourseResponse(course, result)
            	   debug("[StudyRec:" + this + "] sent:" + AreYouAPassCourseResponse(course, result) + " to: " + target);
            	}
            	
              
            }
          }

          
        case AreYouCurrentTermCourseRequest(course, target) =>

          debug("[StudyRec:" + this + "] received: " + AreYouCurrentTermCourseRequest(course, target));
          if (grade != -1) {
            debug("[StudyRec:" + this + "] sent:" + Taken(course, false) + " to " + target);
            sender ! AreYouCurrentTermCourseResponse(course, false);
          } else {
            
            actor {
              offering ! IsYourCourseRequest(course, target)
              println("inner self: " + self)
              self.react {
                case IsYourCourseResponse(course, result, target) => 
                	target ! AreYouCurrentTermCourseResponse(course, result)
                	debug("[StudyRec:" + this + "] sent: " + AreYouCurrentTermCourseResponse(course, result))
              }
            }
          }

       

        case exit =>
          exit
      }
    }
  }

  override def toString(): String = {
    return offering.id + "->" + grade
  }
}