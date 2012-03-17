package domain

import scala.actors.Actor

//////////////////////////STUDENT

trait StudentMessage
case class TakeCourse(offering: Offering, target:Actor) extends StudentMessage
case class ChangeName(name: String) extends StudentMessage
case class HasPassed3(course: Course, target: Actor) extends StudentMessage
case class HasTaken(course: Course, target: Actor)
case class HasPassedPreReqs(course: Course, target: Actor) extends StudentMessage
case class HasPassedPreReqs_FINE_GRAINED(course: Course, target: Actor) extends StudentMessage

trait StudentMessageReply
case class Passed(course: Course, pass: Boolean) extends StudentMessageReply
//Taken : means that the student has taken the course in current term and has not failed or passed it yet
case class Taken(course: Course, pass: Boolean) extends StudentMessageReply
case class PassedPres(course: Course, pass: Boolean) extends StudentMessageReply




//////////////////// OFFERING
trait OfferingMsg
case class IsYourCourseRequest(course: Course, target: Actor) extends OfferingMsg
case class IsYourCourseResponse(course: Course, result:Boolean, target: Actor) extends OfferingMsg


/////////////////// STUDYRECORD
trait StudyRecordMessage
trait StudyRecordMessageReply

case object SayGrade extends StudyRecordMessage
case class AreYouAPassCourseRequest(course: Course, target: Actor) extends StudyRecordMessage
case class AreYouCurrentTermCourseRequest(course: Course, target: Actor) extends StudyRecordMessage
case class AreYouAPassCourseResponse(course: Course, result: Boolean) extends StudyRecordMessage
case class AreYouCurrentTermCourseResponse(course: Course, result: Boolean) extends StudyRecordMessage



class Messages {

}