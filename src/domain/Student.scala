package domain

import scala.actors.Actor;
import scala.actors.Actor._;

//messages:

trait StudentMessage
case class ChangeName(name: String) extends StudentMessage
case class HasPassed1(course: Course, target: Actor) extends StudentMessage
case class HasPassed2(course: Course, target: Actor) extends StudentMessage
case class HasPassedPreReqs(course: Course, target: Actor) extends StudentMessage
case class HasPassedPreReqs_FINE_GRAINED(course: Course, target: Actor) extends StudentMessage

trait StudentMessageReply
case class Passed(course: Course, pass: Boolean) extends StudentMessageReply
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
        case HasPassedPreReqs_FINE_GRAINED(course, target) =>
          debug(this + " received message: " + HasPassedPreReqs_FINE_GRAINED(course, target))
          //for each course in prerequisites -> send a message to all study records.
          for (pre <- course.preRequisites) {
            sendToStudyReqs(HasPassed1(pre, self))
          }
        //initialize counters or maps so that we know when all messages have bin answered
        //but how do we know that the counters are not from previous HasPassedPreReqs_FINE_GRAINED?)
        //maybe it is better to separate contexts of each message. prepare responses for each service 
        //message and then start receiving other messages (?)

        //////////          
        case HasPassed1(course, target) =>
          //APPROACH 1
          debug(this + " received message: " + HasPassed1(course, target))
          //          val result: Boolean = hasPassed(course);
//          val result: Boolean = hasPassedWithFuture(course);
          val result: Boolean = hasPassed(course);
          target ! Passed(course, result)

        //////////          
        case HasPassed2(course, target) =>
          //APPROACH 2
          debug(this + " received message: " + HasPassed2(course, target))
          hasPassed2(course, target);

        case Passed(course, result) =>
          //APPROACH 2
          debug(this + " received message: " + Passed(course, result))

        case HasPassedPreReqs(course, target) =>
          debug(this + " received message: " + HasPassedPreReqs(course, target))
          val result: Boolean = hasPassedPreReqs(course)
          target ! PassedPres(course, result)
        case "sayName" =>
          println(name);
          
        case exit =>
          exit
          
      }
    }
  }

  def sendToStudyReqs(msg: Any) {
    for (sr <- studyRecords) {
      sr ! msg
    }
  }

  def hasPassed(course: Course): Boolean = {

    for (sr <- this.studyRecords) {
      sr ! AreYouAPassCourseRecord(course, self)
    }
    for (sr <- this.studyRecords) {
      receive {
        case Passed(course, true) =>
          debug(this + " received Passed(" + course + ", true)");
          return true;
        case Passed(course, false) =>
          debug(this + " received Passed(" + course + ", false)");
        case other: Any =>
          debug(this + "student received other: " + other);
      }
    }
    return false;
  }

  def hasPassedWithFuture(course: Course): Boolean = {

    val results = for (sr <- this.studyRecords) yield {
      sr !! AreYouAPassCourseRecord(course, self)
    }
    for (i <- 0 until this.studyRecords.size) {
      results(i)() match {
        case Passed(course, true) =>
          debug(this + " received Passed(" + course + ", true)");
          return true;
        case Passed(course, false) =>
          debug(this + " received Passed(" + course + ", false)");
        case other: Any =>
          debug(this + "student received other: " + other);
      }
    }
    return false;
  }

  def hasPassed2(course: Course, target: Actor) = {
    for (sr <- this.studyRecords)
      sr ! AreYouAPassCourseRecord(course, target)
  }

  def hasPassedPreReqs(course: Course): Boolean = {
    for (pre <- course.preRequisites) {
      if (hasPassedWithFuture(pre))
        debug("prerequisite: " + pre + " has been passed by student: " + this)
      else {
        debug("prerequisite: " + pre + " has NOT been passed by student: " + this)
        //        return false
      }

    }
    //all are passed
    return true;
  }
  override def toString = "[Student: id = " + id + ", name = " + name + "]"

}