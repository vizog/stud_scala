package domain

import scala.actors.Actor;
import scala.actors.Actor._;

//messages:

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

case object SayName
case object SayId

class Student(
  var id: String,
  var name: String,
  var studyRecords: List[StudyRecord]) extends BaseDomainClass {
  def this() = this(null, null, Nil)

  override def act() {
    loop {
      react {
        //////////
        case TakeCourse(offering, target) =>
          //validate (has not passed the course itself, has passed pre reqs ...)
          var takeCourse = new StudentTakeCourseActor(this)
          takeCourse.start();
          takeCourse ! TakeCourse(offering, target)
    
        case HasPassed3(course, target) =>
          //APPROACH 3
          debug(this + " received message: " + HasPassed3(course, target))
          val coursePassActor  = new StudentCoursePassActor(this)
          coursePassActor.start
          coursePassActor ! HasPassed3(course, target)
          
          
        case HasTaken(course, target) =>
          //APPROACH 3
          debug(this + " received message: " + HasTaken(course, target))
          val courseTakenCheckActor  = new StudentCourseTakenCheckActor(this)
          courseTakenCheckActor.start
          courseTakenCheckActor ! HasTaken(course, target)
          
          

        case exit =>
          exit
      }
    }
  }

  override def toString = "[Student: id = " + id + ", name = " + name + "]"

}