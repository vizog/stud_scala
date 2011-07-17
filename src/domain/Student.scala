package domain

import scala.actors.Actor;

//messages:

case object SayName
case object SayId

class Student(
  var id: String,
  var name: String,
  var studyRecords: List[StudyRecord]) extends Actor {
  def this() = this(null, null, Nil)

  override def act() {
    loop {
      react {
        case SayName =>
          println(name)
        case SayId =>
          println(id)
        case exit =>
          exit
      }
    }
  }

}