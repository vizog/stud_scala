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
  var preRequisites: List[Course]) extends BaseDomainClass {
  def this() = this(null, null, 0, Nil)

  override def act() {
    loop {
      react {
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

  override def toString = "[course: id= " + id +", name " + name + ", units = " + units + "]"
  override def equals(other: Any): Boolean =
    other match {
      case that: Course =>
        return (that canEqual this) &&
          id == that.id
          
      case _ => false
    }

  def canEqual(other: Any): Boolean =
    other.isInstanceOf[Course]
  
}