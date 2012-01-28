package test.domain
import repository.CourseRepository;
import repository.StudentRepository;
import scala.actors.Actor;
import scala.actors.Actor._;
import java.util.Date;
import domain.Course;
import domain.SayName;
import domain.ChangeName;
import domain.Save;
import domain.Student;
import domain.HasPassed1;
import domain.HasPassedPreReqs;

case class TestCase(testFunc: () => Unit)

class TesterActor extends Actor {

  override def act() {
    loop {
      react {
        case TestCase(testFunc) =>
          testFunc()
        case exit =>
          exit
      }
    }
  }

}

object TestDomain extends {

  def main(args: Array[String]): Unit = {

    var testerActor = new TesterActor
    testerActor.start
//    testerActor ! testStudentHasPassed
    testerActor ! testStudentHasPassedPres
//    //    testerActor ! exit
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

  private def testStudent() {
    var course: Course = CourseRepository.findById("ds");
    val pres: List[Course] = CourseRepository.findPreRequisitesForCourse(course)
    course.preRequisites = pres
    course.start
    var st = StudentRepository.findById("bebe")
    st.studyRecords = StudentRepository.findStudyRecords(st)
    st.start
    st ! HasPassed1(course, self)
    receive {
      case "hello" =>
        println("dsf")
    }
  }
  private def testStudentHasPassed() {
    var course: Course = CourseRepository.findById("ap");
    val pres: List[Course] = CourseRepository.findPreRequisitesForCourse(course)
    course.preRequisites = pres
    course.start
    var st = StudentRepository.findById("bebe")
    st.studyRecords = StudentRepository.findStudyRecords(st)
    st.start
    st ! HasPassed1(course, self)

    receive {
      case "hello" =>
        println("dsf")
    }
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
      case result:Any =>
        println(result)
    }
  }

}