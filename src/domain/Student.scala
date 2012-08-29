package domain

import scala.actors.Actor
import scala.actors.Actor._;
import repository.StudentRepository

//messages:

case object SayName
case object SayId

class Student(
  var id: String, var name: String,
  var studyRecords: List[StudyRecord] = Nil,
  val program: Program) extends BaseDomain {
  def this() = this(null, null, Nil, null)

  override def act() {
    loop {
      react {
        //////////
        case TakeCourse(offering, target) =>
          //validate (has not passed the course itself, has passed pre reqs ...)
          var takeCourse = new StudentTakeCourseActor(this)
          takeCourse.start();
          takeCourse ! TakeCourse(offering, target)

        case HasPassed(course, target) =>
          /* */ debug(this + " received message: " + HasPassed(course, target))
          if (studyRecords == Nil)
        	  studyRecords = StudentRepository.findStudyRecords(this)
          val coursePassActor = new StudentCoursePassActor(this, studyRecords.size, target)
          coursePassActor.start
             for (sr <- studyRecords)
            	 sr ! AreYouAPassCourseRequest(course, coursePassActor)
  

        case HasTaken(course, target) =>

          /* */ debug(this + " received message: " + HasTaken(course, target))
          if (studyRecords == Nil)
            studyRecords = StudentRepository.findStudyRecords(this)

          val courseTakenCheckActor = new StudentCourseTakenCheckActor(this, course, studyRecords.size, target)
          courseTakenCheckActor.start
          //          courseTakenCheckActor ! HasTaken(course, target)
          for (sr <- studyRecords)
            sr ! AreYouCurrentTermCourseRequest(course, courseTakenCheckActor)

        case GPARequest(_, term: Term, target: Actor, _) =>
          if (studyRecords == Nil)
            studyRecords = StudentRepository.findStudyRecords(this)

          /* */ debug(this + " received message: " + GPARequest(null, term: Term, target: Actor, null))
          val gpaCoordinator: StudentComputeTermGPAActor = new StudentComputeTermGPAActor(this, term, studyRecords.size, target)
          gpaCoordinator.start
          //          gpaCoordinator ! GPARequest(this, term, target, null)
          for (sr <- studyRecords)
            sr ! CourseGradeRequest(term, gpaCoordinator, null)

        case NumOfTermTakenUnitsAssertionRequest(target, course) =>
          if (studyRecords == Nil)
            studyRecords = StudentRepository.findStudyRecords(this)
          val currentTermUnitsChecker = new StudentNumOfUnitsValidatorActor(this, course, studyRecords.size, target);
          currentTermUnitsChecker.start
          for (sr <- studyRecords) 
            sr ! NumOfTermTakenUnitsAssertionRequest(currentTermUnitsChecker, course)

      }
    }
  }

  override def toString = "[Student:" + id + "]"

}