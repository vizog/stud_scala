package domain

import scala.actors.Actor;
import util.LoggingSupport

//messages:

trait StudentMessage
case object SayName
case object SayId
case class ChangeName(name: String) extends StudentMessage

class Student(
  var id: String,
  var name: String,
  var studyRecords: List[StudyRecord]) extends BaseDomainClass{
  def this() = this(null, null, Nil)

  override def act() {
    loop {
      react {
        case SayName =>
          println(name)
          debug("student debugging ...")
        case SayId =>
          println(id)
        case ChangeName(newName) =>
          name = newName
        case exit =>
          exit
      }
    }
  }

}