package domain

import scala.actors.Actor

//////////////////////////STUDENT

trait StudentMessage
case class TakeCourse(offering: Offering, target:Actor) extends StudentMessage
case class TakeCourseResponse(result: Boolean, comment: String) extends StudentMessage
case class ChangeName(name: String) extends StudentMessage
case class HasPassed(course: Course, target: Actor) extends StudentMessage
case class HasTaken(course: Course, target: Actor)
case class HasPassedPreReqs(student: Student, course: Course, target: Actor) extends StudentMessage
case class StudentStudyRecordsForTermRequest(term: Term, target: Actor) extends StudentMessage
case class StudentStudyRecordsForTermResponse(records:List[StudyRecord]) extends StudentMessage
case class CourseGradeRequest(term: Term, target: Actor, response:CourseGradeResponse) extends StudentMessage
case class CourseGradeResponse(isForTerm:Boolean, grade: Double, courseName:String, units:Int) extends StudentMessage
case class GPARequest(st:Student, term: Term, target: Actor, result:CourseGradeRequest) extends StudentMessage
case class TranscriptRequest(st:Student, term: Term, target: Actor, result:CourseGradeRequest) extends StudentMessage
case class GPAResponse(gpa:Double) extends StudentMessage
//#ADDED
case class NumOfTermTakenUnitsAssertionRequest(target: Actor) extends StudentMessage
case class NumOfTermTakenUnits(numOfTakenUnits:Int) extends StudentMessage
case class NumOfTermTakenUnitsAssertionResult(assertionPassed:Boolean, takenUnits:Int) extends StudentMessage
//###

trait StudentMessageReply
case class Passed(course: Course, pass: Boolean) extends StudentMessageReply
//Taken : means that the student has taken the course in current term and has not failed or passed it yet
case class Taken(course: Course, pass: Boolean) extends StudentMessageReply
case class PassedPres(requirement: Requirement, pass: Boolean) extends StudentMessageReply




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

/////////////////// TERM
case object TermOfferingsRequest
case class TermOfferingsResponse(offerings: List[Offering])

case class ConditionMessage
case class ConditionalContinuation(condition:ConditionMessage, assertionResponse:Any, assertionTarget: Actor, continuationTarget: Actor, continuationMessage: Any)


class Messages {

}