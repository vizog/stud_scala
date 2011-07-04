package domain

import scala.actors.Actor;

//messages:

case object SayFirstName
case object SayLastName

class Student extends Actor {
  var fName = "ali"
  var lName = "gholi"

  def act() {
    loop {
      react {
        case SayFirstName =>
          println(fName)
        case SayLastName =>
          println(lName)
        case exit =>
          exit
      }
    }
  }

}