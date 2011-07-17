package domain
import actors.Actor;
import java.sql.Date;

class Term(
  var name: String,
  var startDate: Date,
  var offerings: List[Offering]) extends Actor {

  override def act() {
    println(startDate)
  }
}