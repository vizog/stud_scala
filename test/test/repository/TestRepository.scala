package test.repository
import repository.CourseRepository
import repository.StudentRepository
import scala.actors.Actor
import scala.actors.Actor._
import java.util.Date
import domain.Course
import domain.SayName
import domain.ChangeName
import domain.Save
import domain.Student
import domain.HasPassedPreReqs
import domain.HasPassedPreReqs_FINE_GRAINED
import domain.Passed
import util.LoggingSupport
import org.apache.log4j.Logger
import domain.HasPassed

object TestRepository extends {

  def main(args: Array[String]): Unit = {
    testStudentHasPassed2
  }

  protected def testCourseRepo() {
    var course = CourseRepository.findById("ap")
    course ! SayName;
    receive {
      case boogh: String =>
        println("received " + boogh)
    }
    course ! ChangeName("NEW AP");
    course ! SayName
    course ! Save

  }

  private def testFuture() {
    var course: Course = CourseRepository.findById("ap")

    println(new Date() + " sending sayName to course")
    val future = course !! SayName;
    println(new Date() + " calling future ...")
    println("future: " + future())
    println(new Date() + " called future !")

  }

  private def testSync() {
    var course: Course = CourseRepository.findById("ap")
    println(new Date() + " sending sayName to course")
    val response = course !? SayName;
    println(new Date() + " received response: " + response)

  }
  private def testStudentHasPassed2() {
    var course: Course = CourseRepository.findById("ap");
    val pres: List[Course] = CourseRepository.findPreRequisitesForCourse(course)
    course.preRequisites = pres
    course.start
    var st = StudentRepository.findById("bebe")
    st.studyRecords = StudentRepository.findStudyRecords(st)
    st.start
    Logger.getLogger(getClass()).debug("self: " + self)
    st ! HasPassed(course, st)
//    receive {
//      case Passed(course, _) =>
//        Logger.getLogger("Received: " + Passed(course, true))
//      case result: Any =>
//        Logger.getLogger("Received: " + result)
//
//    }
  }
  private def testStudentHasPassedPres() {
    var course: Course = CourseRepository.findById("ds");
    val pres: List[Course] = CourseRepository.findPreRequisitesForCourse(course)
    course.preRequisites = pres
    var st = StudentRepository.findById("bebe")
    st.studyRecords = StudentRepository.findStudyRecords(st)

    st ! HasPassedPreReqs(course, self)
    //    println(pres.size)

    receive {
      case result: Any =>
        println(result)
    }
  }

}