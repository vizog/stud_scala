package domain

import scala.actors.Actor;

//messages:

case object SayGrade

class StudyRecord(
  var grade: Double,
  var offering: Offering) extends Actor {

  def act() {
    loop {
      react {
        case SayGrade =>
          println(grade)
        case exit =>
          exit
      }
    }
  }
}