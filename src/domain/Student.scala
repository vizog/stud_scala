package domain

import scala.actors.Actor;
import scala.actors.Actor._;



class Student(
  var id: String,
  var name: String,
  var studyRecords: List[StudyRecord]) extends BaseDomainClass {
  def this() = this(null, null, Nil)

  override def act() {
    loop {
      react {
        case HasPassed(course, target) =>
          debug(this + " received message: " + HasPassed(course, target))
          //          val result: Boolean = hasPassed(course);
          val result: Boolean = hasPassedWithFuture(course);
          target ! Passed(course, result)

        case HasPassedPreReqs(course, target) =>
          debug(this + " received message: " + HasPassedPreReqs(course, target))
          val result: Boolean = hasPassedPreReqs(course)
          target ! PassedPres(course, result)
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
      sr ! AreYouAPassCourseRequest(course, self)
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
      sr !! AreYouAPassCourseRequest(course, self)
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