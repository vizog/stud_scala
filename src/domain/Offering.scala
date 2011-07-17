package domain

import scala.actors.Actor
import java.sql.Date

//messages:

class Offering(
  var id: String,
  var course: Course,
  var section: Int,
  var examDate: Date,
  var term: Term) extends Actor {

  override def act() {
    loop {
      react {
        case SayId =>
          println(id)
        case exit =>
          exit
      }
    }
  }
}