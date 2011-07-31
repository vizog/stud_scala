package test.repository
import repository.CourseRepository
import domain.SayName
import domain.ChangeName
import scala.actors.Actor
import scala.actors.Actor._
import domain.Save
import java.util.Date
import domain.Course

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

object TestRepository extends {

  def main(args: Array[String]): Unit = {

    var testerActor = new TesterActor
    testerActor.start
    testerActor ! testSync
    //    testerActor ! exit
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

}