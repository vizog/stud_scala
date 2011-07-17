package domain
import actors.Actor;
import java.sql.Date;

class Course(
  var id: String,
  var name: String,
  var units: Int,
  var preRequisites: List[Course]) extends Actor {

  override def act() {
    loop {
      react {
        case SayName =>
          println(this)
        case SayId =>
          println(id)
        case exit =>
          exit
      }
    }

  }

  override def toString = "name: " + name + " units: " + units

}