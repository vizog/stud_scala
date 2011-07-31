package domain
import actors.Actor
import java.sql.Date
import repository.CourseRepository
import util.LoggingSupport

case object Save

class Course(
  var id: String,
  var name: String,
  var units: Int,
  var preRequisites: List[Course]) extends Actor with LoggingSupport {

  override def act() {
    loop {
      react {
        case SayName =>
          println(new java.util.Date() + " course  " + this + " received SayName")
          debug("waiting for 10 secs ... ")
          Thread.sleep(10000)
          reply(name)
        case SayId =>
          println(id)
        case ChangeName(newName) =>
          println("changing the old name : '" + name + "' to new name : '" + newName + "'")
          name = newName
        case Save =>
          CourseRepository.save(this)
        case exit =>
          exit
      }
    }

  }

  override def toString = "[name: " + name + " units: " + units + "]"

}