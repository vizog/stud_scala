package domain

import scala.actors.Actor;
import scala.actors.Actor._;

//messages:


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